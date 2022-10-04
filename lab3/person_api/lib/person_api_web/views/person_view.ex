defmodule PersonApiWeb.PersonView do
  use PersonApiWeb, :view
  alias PersonApiWeb.PersonView

  def render("index.json", %{persons: persons}) do
    %{data: render_many(persons, PersonView, "person.json")}
  end

  def render("show.json", %{person: person}) do
    %{data: render_one(person, PersonView, "person.json")}
  end

  def render("person.json", %{person: person}) do
    %{
      id: person.id,
      key: person.key,
      name: person.name
    }
  end
end
