package observer;

import strategy.LoggerStrategy;

public class LoggerObserver implements Observer {

    private LoggerStrategy strategy;

    public LoggerObserver(LoggerStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(LoggerStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void update(String action) {
        strategy.log(action);
    }
}
