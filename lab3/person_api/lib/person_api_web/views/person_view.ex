defmodule PersonApiWeb.PersonView do
    use PersonApiWeb, :view

    alias PersonApi.KafkaProducer
    alias PersonApi.LocalDatabase

    def render("list.json", _data) do
        KafkaProducer.send_my_message("REST", to_string(:os.system_time(:millisecond)), "Person-API (Elixir): Inquiry for a list of all persons")
        LocalDatabase.get_db()
    end     

    def render("find_by_name.json", %{name: name}) do
        KafkaProducer.send_my_message("REST", to_string(:os.system_time(:millisecond)), "Person-API (Elixir): Inquiry for person with name: " <> name)
        person_db = LocalDatabase.get_db()
        LocalDatabase.find_by_name(person_db, length(person_db), name, []) 
    end

    def render("find_by_key.json", %{key: key}) do
        KafkaProducer.send_my_message("REST", to_string(:os.system_time(:millisecond)), "Person-API (Elixir): Inquiry for person with person key: " <> key)
        person_db = LocalDatabase.get_db()
        LocalDatabase.find_by_key(person_db, length(person_db), key) 
    end

end