defmodule PersonApiWeb.PersonController do
    use PersonApiWeb, :controller

    def index(conn, _params) do
        render(conn, "index.json")
    end
end