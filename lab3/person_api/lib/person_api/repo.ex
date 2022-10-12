defmodule PersonApi.Repo do
  use Ecto.Repo,
    otp_app: :person_api,
    adapter: Ecto.Adapters.Postgres
end
