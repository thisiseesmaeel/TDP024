defmodule PersonApiWeb.PersonController do

  use PersonApiWeb, :controller

  alias PersonApi.Society
  alias PersonApi.Society.Person

  action_fallback PersonApiWeb.FallbackController

  def index(conn, _params) do
    persons = Society.list_persons()
    render(conn, "index.json", persons: persons)
  end

  def create(conn, %{"person" => person_params}) do
    with {:ok, %Person{} = person} <- Society.create_person(person_params) do
      conn
      |> put_status(:created)
      |> put_resp_header("location", Routes.person_path(conn, :show, person))
      |> render("show.json", person: person)
    end
  end

  def show(conn, %{"id" => id}) do
    ##person = Society.get_person!(id)
    test = %{"1" => "Hadi"}
    test
    ##render(conn, "show.json", test)
  end

  def update(conn, %{"id" => id, "person" => person_params}) do
    person = Society.get_person!(id)

    with {:ok, %Person{} = person} <- Society.update_person(person, person_params) do
      render(conn, "show.json", person: person)
    end
  end

  def delete(conn, %{"id" => id}) do
    person = Society.get_person!(id)

    with {:ok, %Person{}} <- Society.delete_person(person) do
      send_resp(conn, :no_content, "")
    end
  end

end
