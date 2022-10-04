defmodule PersonApiWeb.PersonView do
    use PersonApiWeb, :view
    
    def render("index.json", _data) do
        %{"key" => "1", "name" => "Kalbi"}       
    end
end