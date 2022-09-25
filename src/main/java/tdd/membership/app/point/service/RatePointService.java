package tdd.membership.app.point.service;

public class RatePointService implements PointService{

    @Override
    public int calculateAmount(int price) {
        return 100;
    }
}
