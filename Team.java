import java.util.ArrayList;
public class Team
{
    String name;
    String id;
    ArrayList<String> players;
    ArrayList<String> altIDs;
    String region;
    public Team(String n, String i, ArrayList<String> p, String r)
    {
        name = n;
        id = i;
        players = p;
        region = r;
        altIDs = new ArrayList<String>();
    }
    
    public String getJSON()
    {
        String json = "\n				{\n					\"name\": \"" + name + "\",\n					\"id\": \"" + id + "\",\n					\"region\": \"" + region + "\",\n					\"players\": [";
        for(int i=0; i<players.size(); i++){
            json += "\"" + players.get(i) + "\"";
            if(i < players.size() - 1){
                json += ", ";
            }
        }
        json += "]\n				}";
        return json;
    }
    
    public void addAltID(String id){
        altIDs.add(id);
    }
}
