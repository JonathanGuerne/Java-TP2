package ch.arc.tp2.Packets;

import java.io.File;

/*
 * Project Name : ServerProgram
 * author : jonathan.guerne & anthony fleury
 * creation date : 22.05.2017
*/
public class FileMessage implements Packet
{
    private static final long serialVersionUID = 1199932229201799095L;

    public String author;

    public String filename;

    public byte[] content;

    @Override
    public String toString()
    {
        return author+" sent "+filename+" ("+content.length+" byte)";
    }
}
