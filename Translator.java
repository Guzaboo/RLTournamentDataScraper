import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.net.URL;
import java.io.InputStream;
public class Translator
{
    public static String[] eu = {"Germany", "Scotland", "Italy", "Sweden", "Netherlands", "Switzerland", "England", "France", "Austria", "Belgium", "Czech_Republic", "Denmark", "Finland", "Hungary", "Ireland", "Norway", "Poland", "Portugal", "Slovenia", "Spain", "Wales"};
    public static String[] na = {"United_States", "Canada", "Mexico"};
    public static String[] oce = {"Australia", "New_Zealand"};
    public static String[] sam = {"Brazil", "Colombia", "Uruguay", "Argentina", "Chile"};
    
    public static void main(String[] args) throws IOException
    {
        ArrayList<Tournament> tournaments = new ArrayList<Tournament>();
        for(int i=0; i <= 75; i++){
            String file = new String(Files.readAllBytes(Paths.get("Tournaments/" + i + ".html")), StandardCharsets.UTF_8);
            String lowerFile = file.toLowerCase();
            int nameStart = file.indexOf("Template:Infobox league\">h</a>]</span>")+38;
            int nameEnd = file.indexOf("</div>", nameStart);
            String name = file.substring(nameStart, nameEnd);
            System.out.println(name);
            
            int typeStart = file.indexOf("Type:</div>\n<div class=\"infobox-cell-2\">")+40;
            int typeEnd = file.indexOf("</div>", typeStart);
            String type = file.substring(typeStart, typeEnd);
            System.out.println(type);
            boolean lan = type.equals("Offline");
            System.out.println(lan);
            if(!lan && !type.equals("Online")){
                throw new Error("Unknown type: "+type);
            }
            
            int tierStart = file.indexOf("Liquipedia Tier:</div>\n<div class=\"infobox-cell-2\"><a href=\"/rocketleague/")+74;
            int tierEnd = file.indexOf("_Tournaments", tierStart);
            String tier = file.substring(tierStart, tierEnd);
            System.out.println(tier);
            boolean premier = tier.equals("Premier");
            System.out.println(premier);
            if(!premier && !tier.equals("Major")){
                throw new Error("Unknown tier: "+tier);
            }
            
            //teams
            int part = file.indexOf("<h2><span class=\"mw-headline\" id=\"Participants\">Participants");
            int index = file.indexOf("<div class=\"teamcard\"", part);
            int endIndex = file.indexOf("</div></div>\n</div></div>", index);
            int check = file.indexOf("content2", endIndex);
            if(check != -1){
                endIndex = file.indexOf("</div></div>\n</div>\n</div>", check);
            }
            ArrayList<Team> teams = new ArrayList<Team>();
            while(index != -1){
                index = file.indexOf("<a href=", index);
                
                int start = file.indexOf("\"", index)+1;
                int end = file.indexOf("\"", start);
                String id = file.substring(start, end);
                if(id.indexOf("?title") == -1){
                    String url = "https://liquipedia.net" + id;
                    URLConnection con = new URL(url).openConnection();
                    con.connect();
                    InputStream is = con.getInputStream();
                    String redURL = con.getURL().toString();
                    System.out.println( "redirected url: " + redURL );
                    id = redURL.substring(22, redURL.length());
                    is.close();
                }
                
                index = file.indexOf("title=\"", index);
                start = file.indexOf(">", index)+1;
                end = file.indexOf("<", start);
                String teamName = file.substring(start, end);
                System.out.println(teamName);
                            if(teamName.equals("Ascension")){
                                int f=3;
                            }
                int finalIndex = file.indexOf("<div class=\"teamcard\"", index);
                int coachIndex = file.indexOf("<abbr title=\"Coach\">C", index);
                
                start = file.indexOf("<tbody><tr><th>1</th><td><a href=\"/rocketleague/Category:", index)+57;
                end = file.indexOf("\"", start);
                String country = file.substring(start, end);
                String region = findRegion(country);
                
                start = file.indexOf("&#160;<a href=\"/rocketleague/", end)+29;
                ArrayList<String> players = new ArrayList<String>();
                while(start != -1+29 && start < endIndex && (start < finalIndex || finalIndex == -1) && (start < coachIndex || coachIndex == -1)){
                    end = file.indexOf("\"", start);
                    String playerName = file.substring(start, end);
                    if(playerName.indexOf("?title=") != -1){
                        int tempStart = playerName.indexOf("?title=") + 7;
                        int tempEnd = playerName.indexOf("&amp");
                        playerName = playerName.substring(tempStart, tempEnd);
                    } else {
                        
                        String url = "https://liquipedia.net/rocketleague/" + playerName;
                        URLConnection con = new URL(url).openConnection();
                        con.connect();
                        InputStream is = con.getInputStream();
                        String redURL = con.getURL().toString();
                        System.out.println( "redirected url: " + redURL );
                        playerName = redURL.substring(36, redURL.length());
                        is.close();
                        
                    }
                    players.add(playerName);
                    System.out.println(playerName);
                            if(name.equals("FACEIT X Games Rocket League Invitational") && playerName.equals("Torment")){
                                int f=3;
                            }
                    start = file.indexOf("&#160;<a href=\"/rocketleague/", end)+29;
                }
                /*
                String teamLower = teamName.toLowerCase();
                String teamLink = teamLower.replaceAll(" ", "_");
                start = lowerFile.indexOf("class=\"team-template-team2-short\"><span class=\"team-template-text\"><a href=\"/rocketleague/" + teamLink + "\" title=\"" + teamLower + "\">") + 101 + teamName.length()*2;
                end = file.indexOf("<", start);
                String id = file.substring(start, end);*/
                System.out.println(id);
                
                teams.add(new Team(teamName, id, players, region));
                index = finalIndex;
            }
            
                        //alt team ids
                        index = file.indexOf("<dd><sup>");
                        int close = file.indexOf("</dd></dl>", index);
                        while(index != -1){
                            int nextIndex = file.indexOf("<dd><sup>", index+1);
                            index = file.indexOf("<a href=\"", index);
                            ArrayList<String> altIDs = new ArrayList<String>();
                            while(index != -1 && (index < nextIndex || nextIndex == -1) && index < close){
                                int start = index+9;
                                int end = file.indexOf("\"", start);
                                String altID = file.substring(start, end);
                                if(altID.indexOf("?title") == -1){
                                    String url = "https://liquipedia.net" + altID;
                                    URLConnection con = new URL(url).openConnection();
                                    con.connect();
                                    InputStream is = con.getInputStream();
                                    String redURL = con.getURL().toString();
                                    System.out.println( "redirected url: " + redURL );
                                    altID = redURL.substring(22, redURL.length());
                                    is.close();
                                }
                                altIDs.add(altID);
                                index = file.indexOf("<a href=\"", end);
                            }
                            for(int t=0; t < teams.size(); t++){
                                for(int j=0; j < altIDs.size(); j++){
                                    if(teams.get(t).id.equals(altIDs.get(j))){
                                        for(int k=0; k < altIDs.size(); k++){
                                            if(k != j){
                                                teams.get(t).addAltID(altIDs.get(k));
                                            }
                                        }
                                    }
                                }
                            }
                            if(index > close){
                                break;
                            }
                            index = nextIndex;
                        }
            
            //matches
            index = file.indexOf("bracket-popup-header-left");
            index = file.indexOf("team-template-text", index);
            SimpleDateFormat sdf = new SimpleDateFormat("MMMMM d, yyyy - k:mm z");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMMM d, yyyy z");
            SimpleDateFormat sdf3 = new SimpleDateFormat("MMMMM d,yyyy - k:mm z");
            SimpleDateFormat sdf4 = new SimpleDateFormat("MMMMM d, yyyy k:mm z");
            ArrayList<Match> matches = new ArrayList<Match>();
            while(index != -1){
                int start = file.indexOf("<a href=\"", index)+9;
                int end = file.indexOf("\"", start);
                String team1 = file.substring(start, end);
                if(team1.indexOf("?title") == -1){
                    String url = "https://liquipedia.net" + team1;
                    URLConnection con = new URL(url).openConnection();
                    con.connect();
                    InputStream is = con.getInputStream();
                    String redURL = con.getURL().toString();
                    System.out.println( "redirected url: " + redURL );
                    team1 = redURL.substring(22, redURL.length());
                    is.close();
                }
                
                if(name.equals("RLCS Season 2 - North America")){
                    int f=3;
                }
                            for(int a=0; a<teams.size(); a++){
                                if(teams.get(a).id.equals(team1)){
                                    break;
                                }
                                for(int j=0; j<teams.get(a).altIDs.size(); j++){
                                    if(teams.get(a).altIDs.get(j).equals(team1)){
                                        team1 = teams.get(a).id;
                                        break;
                                    }
                                }
                            }
                index = file.indexOf("bracket-popup-header-right", index);
                index = file.indexOf("team-template-text", index);
                start = file.indexOf("<a href=\"", index)+9;
                end = file.indexOf("\"", start);
                String team2 = file.substring(start, end);
                if(team2.indexOf("?title") == -1){
                    try{String url = "https://liquipedia.net" + team2;
                    URLConnection con = new URL(url).openConnection();
                    con.connect();
                    InputStream is = con.getInputStream();
                    String redURL = con.getURL().toString();
                    System.out.println( "redirected url: " + redURL );
                    team2 = redURL.substring(22, redURL.length());
                    is.close();} catch(FileNotFoundException e){}
                }
                
                            for(int a=0; a<teams.size(); a++){
                                if(teams.get(a).id.equals(team2)){
                                    break;
                                }
                                for(int j=0; j<teams.get(a).altIDs.size(); j++){
                                    if(teams.get(a).altIDs.get(j).equals(team2)){
                                        team2 = teams.get(a).id;
                                        break;
                                    }
                                }
                            }
                System.out.println("\n"+team1+" VS "+team2);
                
                index = file.indexOf("bracket-popup-body\"", index);
                index = file.indexOf("<span class=\"timer-object\"", index);
                if(index == -1){ System.out.println("WARNING: index = 0"); }
                start = file.indexOf(">", index)+1;
                end = file.indexOf("<", start);
                String dateStr = file.substring(start, end).trim();
                System.out.println(dateStr);
                long date = 0;
                try {
                    Date d = sdf.parse(dateStr+" GMT");
                    date = d.getTime();
                    System.out.println(date);
                } catch (ParseException e) {
                    try {
                        Date d = sdf2.parse(dateStr+" GMT");
                        date = d.getTime();
                        System.out.println(date);
                    } catch (ParseException e2) {
                        try {
                            Date d = sdf3.parse(dateStr+" GMT");
                            date = d.getTime();
                            System.out.println(date);
                        } catch (ParseException e3) {
                            try {
                                Date d = sdf4.parse(dateStr+" GMT");
                                date = d.getTime();
                                System.out.println(date);
                            } catch (ParseException e4) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                int nextMatchStart = file.indexOf("bracket-popup-header-left", index);
                
                ArrayList<Game> games = new ArrayList<Game>();
                index = file.indexOf("<div style=\"float:left;margin-left:5px;\">", index);
                while(index != -1 && (index < nextMatchStart || nextMatchStart == -1)){
                    start = index + 41;
                    end = file.indexOf("<", start);
                    String s1 = file.substring(start, end);
                    if(s1.equals("")){s1 = "0";}
                    int score1 = Integer.parseInt(s1);
                    
                    index = file.indexOf("<div style=\"float:right;margin-right:5px;\">", index);
                    start = index + 43;
                    end = file.indexOf("<", start);
                    String s2 = file.substring(start, end);
                    if(s2.equals("")){s2 = "0";}
                    int score2 = Integer.parseInt(s2);
                    
                    games.add(new Game(score1, score2));
                    System.out.println(score1 + " - " + score2);
                    
                    index = file.indexOf("<div style=\"float:left;margin-left:5px;\">", index);
                }
                
                index = nextMatchStart;
                //index = file.indexOf("team-template-text", index);
                if(games.size() > 0){
                    matches.add(new Match(date, team1, team2, games));
                }
            }
            tournaments.add(new Tournament(name, lan, premier, teams, matches));
        }
        saveJSON(tournaments);
    }
    
    private static String findRegion(String country){
        for(int c=0; c<eu.length; c++){
            if(country.equals(eu[c])){
                return "eu";
            }
        }
        for(int c=0; c<na.length; c++){
            if(country.equals(na[c])){
                return "na";
            }
        }
        for(int c=0; c<oce.length; c++){
            if(country.equals(oce[c])){
                return "oce";
            }
        }
        for(int c=0; c<sam.length; c++){
            if(country.equals(sam[c])){
                return "sam";
            }
        }
        throw new Error("Could not assign country to region: "+country);
    }
    
    private static void saveJSON(ArrayList<Tournament> tournaments) throws FileNotFoundException
    {
        String json = "{\n	\"tournaments\": [";
        for(int i=0; i<tournaments.size(); i++){
            json += tournaments.get(i).getJSON();
            if(i < tournaments.size() - 1){
                json += ",";
            }
        }
        json += "\n	]\n}";
        
        PrintWriter out = new PrintWriter("RLtournaments.json");
        out.println(json);
        out.close();
    }
}
