package bgu.spl.net.srv;

import java.util.Vector;

public class PMMSG extends MSG {

    private String username;
    private String content;
    private String dateTime;

    public PMMSG (String _username, String _content, String _dateTime){
        super((short)6);
        username = _username;
        content = _content;
        dateTime = _dateTime;
    }

    public String getUsername(){
        return username;
    }
    public String getContent(Vector<String> filter){
        return filter(filter,content);
    }

    public String getDateTime() {
        return dateTime;
    }

    private String filter(Vector<String> filter, String toFilter){
        for (String word : filter)
            toFilter = toFilter.replaceAll(word, "<filtered>");
        return toFilter;
    }
}
