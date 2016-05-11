package main

import (
	_ "github.com/lib/pq"
    "database/sql"
    "encoding/json"
    "fmt"
    "io/ioutil"
    "log"
    "net/http"
    "strconv"
    "strings"
)

type Task struct {
    Id int
    Name string
    Done bool
    ListId int
}

type CreateTaskRequest struct {
    Name string `json:"name"`
    ListId int  `json:"list_id"`
}

type List struct {
    Id int
    Name string
}

type ListCreateRequest struct {
    Name string
}

type ListCreateResponse struct {
    Id int
}

func CheckFatal(err error) {
    if err != nil {
        log.Fatal(err)
    }
}

type Database struct {
    Db *sql.DB
}


// getURLParameter takes out the first URL parameter from the path
// path should be formated as /type/param.
// It returns a parameter representing a string.
func getURLParameter(path string) *string {
    param := strings.Split(path, "/")
    if len(param) == 3 {
        return &param[2]
    } else {
        return nil
    }
}

// getLists retrieves all the lists from the database.
// It returns a slice of List structs.
func getLists(db *sql.DB) []List {
    rows, err := db.Query("select * from list")
    CheckFatal(err)

    // Retrieve all lists from the query
    res := make([]List, 0)
    for rows.Next() {
        list := List{}
        err := rows.Scan(&list.Id, &list.Name)
        CheckFatal(err)
        res = append(res, list)
    }

    return res
}

// getTasks retrieves all the tasks from the database.
// It returns a slice of Task structs.
func getTasks(db *sql.DB, listId int) []Task {
    // Query the database for all tasks that references the specified list
    rows, err := db.Query("select * from task where list=$1", listId)
    CheckFatal(err)

    // Retrieve all tasks from the query
    res := make([]Task, 0)
    for rows.Next() {
        var name string
        var id, list int
        var done bool
        err := rows.Scan(&id, &name, &done, &list)
        CheckFatal(err)
        res = append(res, Task{Id: id, Name: name, Done: done, ListId: list})
    }

    return res
}

// insertList adds a list to the database with listName as its name.
// It returns the Id of the list.
func insertList(db *sql.DB, listName string) int {
    var listId int
    err := db.QueryRow("insert into list (name) values ($1) returning id", listName).Scan(&listId)
    CheckFatal(err)

    return listId
}

// insertTask adds a task to the database. 
// taskName specifies the name of the task, and listId the list that it belongs to.
func insertTask(db *sql.DB, taskName string, listId int) {
    _, err := db.Exec("insert into task (name, list) values ($1, $2)", taskName, listId)
    // Handle non-existing list id
    CheckFatal(err)
}

// listHandler manages requests with regards to the lists.
// A GET request to /list will retrieve all the lists.
// A GET request to /list/<id> will retrieve all the tasks of the list with id <id>.
// A POST request to /list will create a new list with the name provided in the Post Body
// in the format {"name": "listName"}
func (db *Database) listHandler(w http.ResponseWriter, r *http.Request) {
    if r.Method == "GET" {
        // Handle GET Request
        param := getURLParameter(r.URL.Path)

        // If no parameter exists, retrieve all lists
        if param == nil || *param == "" {
            // Retrieve lists
            list := getLists(db.Db)
            json.NewEncoder(w).Encode(&list)
        } else {
            // Get the list id from the parameter
            listId, err := strconv.Atoi(*param)
            CheckFatal(err)

            // Retrieve tasks and send them back
            tasks := getTasks(db.Db, listId)
            json.NewEncoder(w).Encode(&tasks)
        }
    } else if r.Method == "POST" {
        // Parse the request and create a new list
        body, err := ioutil.ReadAll(r.Body)
        CheckFatal(err)
        listRequest := ListCreateRequest{}
        err = json.Unmarshal(body, &listRequest)
        CheckFatal(err)

        listResponse := ListCreateResponse{}
        listResponse.Id = insertList(db.Db, listRequest.Name)

        json.NewEncoder(w).Encode(&listResponse)
    }
}

// taskHandler manages requests with regards to the tasks.
// A POST request to /task will create a new task with the name and list provided in
// the Post Body. The Body should be in the format {"name": "taskName", "list_id": 123}
func (db *Database) taskHandler(w http.ResponseWriter, r *http.Request) {
    if r.Method == "POST" {
        body, err := ioutil.ReadAll(r.Body)
        CheckFatal(err)
        taskRequest := CreateTaskRequest{}
        err = json.Unmarshal(body, &taskRequest)
        CheckFatal(err)

        insertTask(db.Db, taskRequest.Name, taskRequest.ListId)

        fmt.Fprintf(w, "OK")
    }
}

// ConnectDb connects to a postgres database.
// it returns a database handle
func ConnectDb() *sql.DB {
    db, err := sql.Open("postgres", "postgres://simon@localhost/todo?sslmode=disable")
    CheckFatal(err)

    return db
}

// Handlers retrieves all handlers for the server.
func Handlers() *http.ServeMux {
    db := Database{Db: ConnectDb()}
    mux := http.NewServeMux()
    mux.Handle("/list", http.HandlerFunc(db.listHandler))
    mux.Handle("/list/", http.HandlerFunc(db.listHandler))
    mux.Handle("/task", http.HandlerFunc(db.taskHandler))
    return mux
}

func main() {
    // Listen on port 5050
    err := http.ListenAndServe(":5050", Handlers())
    CheckFatal(err)
}
