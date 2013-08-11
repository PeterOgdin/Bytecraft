package info.bytecraft.api;

import lombok.Data;

@Data
public class PaperLog
{
    private String playerName;
    private String date;
    private String action;
    private String material;
    
    public PaperLog(String player, String date, String action, String material)
    {
        setPlayerName(player);
        setDate(date);
        setAction(action);
        setMaterial(material);
    }
}
