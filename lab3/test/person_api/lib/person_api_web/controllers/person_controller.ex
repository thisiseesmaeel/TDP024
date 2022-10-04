defmodule PersonApiWeb.PersonController do
    use PersonApiWeb, :controller

    def index(conn, _params) do
        render(conn, "index.json")
    end

    def lol(conn, _params) do
        render(conn, "index2.json")
    end
end