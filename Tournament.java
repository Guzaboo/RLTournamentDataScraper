import java.util.ArrayList;
public class Tournament
{
    String name;
    boolean lan, premier;
    ArrayList<Team> teams;
    ArrayList<Match> matches;
    public Tournament(String n, boolean l, boolean p, ArrayList<Team> t, ArrayList<Match> m)
    {
        name = n;
        lan = l;
        premier = p;
        teams = t;
        matches = m;
    }
    
    public String getJSON()
    {
        String json = "\n		{\n			\"name\": \"" + name + "\",\n			\"lan\": " + lan + ",\n			\"premier\": " + premier + ",\n			\"teams\": [";
        for(int i=0; i<teams.size(); i++){
            json += teams.get(i).getJSON();
            if(i < teams.size() - 1){
                json += ",";
            }
        }
        json += "\n			],\n			\"matches\": [";
        for(int i=0; i<matches.size(); i++){
            json += matches.get(i).getJSON();
            if(i < matches.size() - 1){
                json += ",";
            }
        }
        json += "\n			]\n		}";
        return json;
    }
}
