package id.go.b4t.gapura;

/**
 * Created by Abraham_Lundy on 21/03/2018.
 */

public class DataTamuModel {

    String id;
    String nama;
    String kartu;

    public String getKartu() {
        return kartu;
    }

    public void setKartu(String kartu) {
        this.kartu = kartu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public DataTamuModel(String id, String nama, String kartu) {
        this.id = id;
        this.nama = nama;
        this.kartu= kartu;
    }


}
