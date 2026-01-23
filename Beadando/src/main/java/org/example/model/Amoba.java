package org.example.model;

public class Amoba {
    private int[][] tabla;

    public Amoba() {
        this.tabla = new int[3][3];
    }

    public int getElem(int sor, int oszlop) {
        return tabla[sor][oszlop];
    }

    public void setElem(int sor, int oszlop, int ertek) {
        tabla[sor][oszlop] = ertek;
    }

    public int win() {
        for (int i = 0; i < 3; i++) {
            if (tabla[i][0] != 0 && tabla[i][0] == tabla[i][1] && tabla[i][1] == tabla[i][2]) {
                return tabla[i][0];
            }
            if (tabla[0][i] != 0 && tabla[0][i] == tabla[1][i] && tabla[1][i] == tabla[2][i]) {
                return tabla[0][i];
            }
        }

        if (tabla[0][0] != 0 && tabla[0][0] == tabla[1][1] && tabla[1][1] == tabla[2][2]) {
            return tabla[0][0];
        }

        if (tabla[0][2] != 0 && tabla[0][2] == tabla[1][1] && tabla[1][1] == tabla[2][0]) {
            return tabla[0][2];
        }

        return 0;
    }

    public int dontetlen() {
        boolean teleVan = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabla[i][j] == 0) {
                    teleVan = false;
                }
            }
        }

        if (teleVan && win() == 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    tabla[i][j] = 0;
                }
            }
            return 1;
        }

        return 0;
    }
}