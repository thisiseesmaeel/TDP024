defmodule PersonApi.Society.Person do
  use Ecto.Schema
  import Ecto.Changeset

  @primary_key {:id, :binary_id, autogenerate: true}
  @foreign_key_type :binary_id
  schema "persons" do
    field :key, :string
    field :name, :string

    timestamps()
  end

  @doc false
  def changeset(person, attrs) do
    person
    |> cast(attrs, [:key, :name])
    |> validate_required([:key, :name])
    |> unique_constraint(:key)
  end
end
