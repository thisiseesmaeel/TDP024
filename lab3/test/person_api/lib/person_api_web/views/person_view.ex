defmodule PersonApiWeb.PersonView do
    use PersonApiWeb, :view
    
    def render("index.json", _data) do
        %{"key" => "1", "name" => "Hadi"}       
    end


    def render("index2.json", _data) do
        [%{"key" => "1", "name" => "Hadi"}, %{"key" => "2", "name" => "Ismail"}]       
    end

end