defmodule PersonApiWeb.PersonController do
    use PersonApiWeb, :controller

    def list(conn, _params) do
        render(conn, "list.json")
    end

    def find_by_name(conn, %{"name" => name }) do
        render(conn, "find_by_name.json", name: name)
    end

    def find_by_key(conn, %{ "key" => key }) do
        render(conn, "find_by_key.json", key: key)
    end

end