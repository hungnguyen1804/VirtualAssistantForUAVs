package com.example.cps_lab411.UavState;

public enum UavMode {
    Connected,
    Manual,
    Position,
    OffBoard,
    Hold,
    TakeOff,
    Land;

    public static UavMode getUavMode(int id) {
        switch (id) {
            case 0:
                return Manual;
            case 1:
                return Position;
            case 2:
                return OffBoard;
            case 3:
                return Land;
            case 4:
                return TakeOff;
            case 5:
                return Hold;
            default:
                return Connected;
        }
    }
}
