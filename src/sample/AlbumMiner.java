/*Class that handles text mining of the url and construction of the JSONArray
 */

package sample;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class AlbumMiner {

    private int submitText;


    //constructor
    public AlbumMiner(String txt){
        this.submitText = Integer.parseInt(txt);
    }

    public JSONArray mine() throws IOException {
        URL strQuery = new URL("https://jsonplaceholder.typicode.com/photos?albumId=" + Integer.toString(submitText));
        JSONArray albums = new JSONArray(txtFromURL(strQuery));
        return albums;
    }

    protected  String txtFromURL(URL strQuery) throws IOException{

        BufferedReader in = new BufferedReader(
                new InputStreamReader(strQuery.openStream()));
        String inputLine;
        StringBuilder builder = new StringBuilder();
        //holding all the info in memory, not good, need better way to parse as I stream the input
        while ((inputLine = in.readLine()) != null) builder.append(inputLine);
        in.close();
        return builder.toString();
    }

}
