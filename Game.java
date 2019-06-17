public class Game
{
    int score1, score2;
    public Game(int s1, int s2)
    {
        score1 = s1;
        score2 = s2;
    }
    
    public String getJSON(String team1, String team2)
    {
        String json = "\n						{\n							\"" + team1 + "\": " + score1 + ",\n							\"" + team2 + "\": " + score2 + "\n						}";
        return json;
    }
}
