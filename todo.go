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

func (db *Database) listHandler(w http.ResponseWriter, r *http.Request) {
    // Retrieve lists
    list := getLists(db.Db)
    json.NewEncoder(w).Encode(&list)
}

func ConnectDb() *sql.DB {
    db, err := sql.Open("postgres", "postgres://simon@localhost/todo?sslmode=disable")
    CheckFatal(err)

    return db
}

func Handlers() *http.ServeMux {
    db := Database{Db: ConnectDb()}
    mux := http.NewServeMux()
    mux.Handle("/list", http.HandlerFunc(db.listHandler))
    return mux
}

func main() {
    // Listen on port 5050
    err := http.ListenAndServe(":5050", Handlers())
    CheckFatal(err)
}
