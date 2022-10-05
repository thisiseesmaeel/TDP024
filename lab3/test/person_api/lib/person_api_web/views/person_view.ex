defmodule PersonApiWeb.PersonView do
    use PersonApiWeb, :view

    # Iteration through the list of person and search for a person
    # with specified name
    def find_by_name(list, size, _name, result) when size <= 0 do
        IO.puts "Finish"
        result
    end 

    def find_by_name(list, size, name, result) do
        element = Enum.at(list, size - 1)
        IO.puts(element["name"])

        if element["name"] == name do
            IO.puts "Inserting"
            result = result ++ [element]
            IO.inspect result
            find_by_name(list, size - 1, name, result)
        else
            find_by_name(list, size - 1, name, result)
        end 
    end 


    # Iteration through the list of person and search for a person
    # with specified key
    def find_by_key(list, size, _key) when size <= 0 do
        IO.puts "Finish"
        nil
    end 

    def find_by_key(list, size, key) do
        element = Enum.at(list, size - 1)
        if element["key"] == key do
            IO.puts "Found"
            element
        else
            IO.puts(element["key"])
            find_by_key(list, size - 1, key)
        end
    end 

    def get_db()do
        [%{"key" => "1", "name" => "Jakob Pogulis"}, %{"key" => "2" ,"name" => "Xena"}, %{"key" => "3", "name" => "Marcus Bendtsen"}, %{"key" => "4", "name" => "Zorro"}, %{"key" => "5", "name" => "Q"}]
    end
    
    def render("index.json", _data) do
        %{"key" => "1", "name" => "Hadi"}       
    end

    def render("list.json", _data) do
        get_db()
    end     

    def render("find_by_name.json", %{name: name}) do
        person_db = get_db()
        result = []
        find_by_name(person_db, length(person_db), name, result) 
    end

    def render("find_by_key.json", %{key: key}) do
        person_db = get_db()
        find_by_key(person_db, length(person_db), key) 
    end

end