package udacityteam.healthapp.completeRedesign.Data.Networking.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vvost on 11/21/2017.
 */

public class Model implements Parcelable{

    private String name;
    private String offset;
    private String id;

    public Model()
    {
        this.name = name;
        this.offset = offset;
        this.id = id;
    }
    public Model(String name, String offset, String id)
    {
        this.name = name;
        this.offset = offset;
        this.id = id;
    }


    protected Model(Parcel in) {
        name = in.readString();
        offset = in.readString();
        id = in.readString();
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        //      return super.toString();
        return name;
    }

    public String toString3()
    {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(offset);
        dest.writeString(id);
    }
}
