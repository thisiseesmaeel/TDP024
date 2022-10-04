defmodule PersonApi.SocietyFixtures do
  @moduledoc """
  This module defines test helpers for creating
  entities via the `PersonApi.Society` context.
  """

  @doc """
  Generate a unique person key.
  """
  def unique_person_key, do: "some key#{System.unique_integer([:positive])}"

  @doc """
  Generate a person.
  """
  def person_fixture(attrs \\ %{}) do
    {:ok, person} =
      attrs
      |> Enum.into(%{
        key: unique_person_key(),
        name: "some name"
      })
      |> PersonApi.Society.create_person()

    person
  end
end
