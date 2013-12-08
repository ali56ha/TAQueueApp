package utah.edu.cs4962.TAQueue;

/**
 * Created by shong on 12/7/13.
 */
public class QueueClientFactory
{
    private static QueueClient _client;

    public synchronized static QueueClient getInstance() {

        if (_client != null)
            return _client;

        _client = new QueueClient();

        return _client;
    }
}
