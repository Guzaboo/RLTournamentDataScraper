import java.util.ArrayList;
public class Match
{
    long date;
    String team1, team2;
    ArrayList<Game> games;
    public Match(long d, String t1, String t2, ArrayList<Game> g)
    {
        date = d;
        team1 = t1;
        team2 = t2;
        games = g;
    }
    
    public String getJSON()
    {
        String json = "\n				{\n					\"date\": " + date + ",\n					\"teams\": [\"" + team1 + "\", \"" + team2 + "\"],\n					\"games\": [";
        for(int i=0; i<games.size(); i++){
            json += games.get(i).getJSON(team1, team2);
            if(i < games.size() - 1){
                json += ",";
            }
        }
        json += "\n					]\n				}";
        return json;
    }
}
