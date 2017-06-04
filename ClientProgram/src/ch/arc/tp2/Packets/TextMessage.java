package ch.arc.tp2.Packets;

/**
 * Project Name : ClientProgram
 * @author anthony.fleury, guerne.jonathan
 * @date  20.05.2017
 */
public class TextMessage implements Packet
{

    private static final long serialVersionUID = -451330142184868255L;
    public String author;
    public String message;

    @Override
    public String toString()
    {
        return author+" : "+message;
    }
}
