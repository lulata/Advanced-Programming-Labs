package NP.MidTerm2.lab13;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}
interface WeatherObserver{
    void update(float temperature, float humidity, float pressure);
}

interface Subject{
    void register(WeatherObserver weatherObserver);
    void remove(WeatherObserver weatherObserver);
    void notify(float temperature, float humidity, float pressure);
}
class WeatherDispatcher implements Subject{
    List<WeatherObserver> weatherObserverList;
    float temperature;
    float humidity;
    float pressure;

    public WeatherDispatcher(){
        weatherObserverList = new ArrayList<>();
    }
    @Override
    public void register(WeatherObserver weatherObserver) {
        if(!weatherObserverList.contains(weatherObserver))
            weatherObserverList.add(weatherObserver);
    }

    @Override
    public void remove(WeatherObserver weatherObserver) {
        if(weatherObserverList.indexOf(weatherObserver) >= 0)
            weatherObserverList.remove(weatherObserver);
    }

    @Override
    public void notify(float temperature, float humidity, float pressure) {
        weatherObserverList.stream().sorted((w1,w2) -> {
            if(w1 instanceof CurrentConditionsDisplay)
                return -1;
            return 1;
        }).forEach(weatherObserver -> {
            weatherObserver.update(temperature, humidity, pressure);
        });
        System.out.println();
    }
    public void setMeasurements(float temperature, float humidity, float pressure){
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        notify(temperature, humidity, pressure);
    }
}

class ForecastDisplay implements WeatherObserver{
    Subject weatherDispatcher;
    float pressure;
    public ForecastDisplay(Subject weatherDispatcher){
        this.weatherDispatcher = weatherDispatcher;
        weatherDispatcher.register(this);
        pressure = 0;
    }
    @Override
    public void update(float temperature, float humidity, float pressure) {
        if(pressure > this.pressure){
            System.out.println("Forecast: Improving");
        }else if(pressure < this.pressure){
            System.out.println("Forecast: Cooler");
        }else{
            System.out.println("Forecast: Same");
        }
        this.pressure = pressure;
    }
}

class CurrentConditionsDisplay implements WeatherObserver{
    float humidity;
    float temperature;
    Subject weatherDispatcher;
    public CurrentConditionsDisplay(Subject weatherDispatcher){
        this.weatherDispatcher = weatherDispatcher;
        weatherDispatcher.register(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.humidity = humidity;
        this.temperature = temperature;

        System.out.println("Temperature: " + this.temperature + "F");
        System.out.println("Humidity: " + this.humidity + "%");

    }
}
