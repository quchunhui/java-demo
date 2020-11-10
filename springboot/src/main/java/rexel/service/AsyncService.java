package rexel.service;

import java.util.concurrent.Future;

public interface AsyncService {
    void returnNone();
    Future<String> returnJson(int i);
}
