defmodule PersonApi.SocietyTest do
  use PersonApi.DataCase

  alias PersonApi.Society

  describe "persons" do
    alias PersonApi.Society.Person

    import PersonApi.SocietyFixtures

    @invalid_attrs %{key: nil, name: nil}

    test "list_persons/0 returns all persons" do
      person = person_fixture()
      assert Society.list_persons() == [person]
    end

    test "get_person!/1 returns the person with given id" do
      person = person_fixture()
      assert Society.get_person!(person.id) == person
    end

    test "create_person/1 with valid data creates a person" do
      valid_attrs = %{key: "some key", name: "some name"}

      assert {:ok, %Person{} = person} = Society.create_person(valid_attrs)
      assert person.key == "some key"
      assert person.name == "some name"
    end

    test "create_person/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Society.create_person(@invalid_attrs)
    end

    test "update_person/2 with valid data updates the person" do
      person = person_fixture()
      update_attrs = %{key: "some updated key", name: "some updated name"}

      assert {:ok, %Person{} = person} = Society.update_person(person, update_attrs)
      assert person.key == "some updated key"
      assert person.name == "some updated name"
    end

    test "update_person/2 with invalid data returns error changeset" do
      person = person_fixture()
      assert {:error, %Ecto.Changeset{}} = Society.update_person(person, @invalid_attrs)
      assert person == Society.get_person!(person.id)
    end

    test "delete_person/1 deletes the person" do
      person = person_fixture()
      assert {:ok, %Person{}} = Society.delete_person(person)
      assert_raise Ecto.NoResultsError, fn -> Society.get_person!(person.id) end
    end

    test "change_person/1 returns a person changeset" do
      person = person_fixture()
      assert %Ecto.Changeset{} = Society.change_person(person)
    end
  end
end
