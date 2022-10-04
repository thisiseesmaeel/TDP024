defmodule PersonApi.Repo.Migrations.CreatePersons do
  use Ecto.Migration

  def change do
    create table(:persons, primary_key: false) do
      add :id, :binary_id, primary_key: true
      add :key, :string
      add :name, :string

      timestamps()
    end

    create unique_index(:persons, [:key])
  end
end
