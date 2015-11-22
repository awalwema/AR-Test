package com.example.andrew.ar_test.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.example.andrew.ar_test.ui.IconMarker;
import com.example.andrew.ar_test.ui.Marker;
import com.jwetherell.augmented_reality.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class should be used as a example local data source. It is an example of
 * how to add data programatically. You can add data either programatically,
 * SQLite or through any other source.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class LocalDataSource extends DataSource {

    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    private static Bitmap icon = null;

    public LocalDataSource(Resources res) {
        if (res == null) throw new NullPointerException();

        createIcon(res);
    }

    protected void createIcon(Resources res) {
        if (res == null) throw new NullPointerException();

        icon = BitmapFactory.decodeResource(res, R.drawable.oubear);
    }

    public List<Marker> getMarkers() {
        Marker SFH = new IconMarker("South Foundation Hall", 42.673648896361826, -83.21774147450924, 0, Color.YELLOW, icon, 2);
        cachedMarkers.add(SFH);

        Marker EC = new IconMarker("Engineering Center", 42.67175571785303, -83.2150337854087, 0, Color.YELLOW, icon, 476);
        cachedMarkers.add(EC);

        Marker HHB = new IconMarker("Human Health Building", 42.6777462440628304, -83.21962170302868, 0, Color.YELLOW, icon, 14);

        cachedMarkers.add(HHB);

        Marker REC = new IconMarker("Rec Center", 42.673605265162614, -83.2130965590477, 0, Color.YELLOW, icon, 17);
        cachedMarkers.add(REC);

        Marker NFH = new IconMarker("North Foundation Hall", 42.67426835748442, -83.21780283004045, 0, Color.YELLOW, icon, 34);
        cachedMarkers.add(NFH);

        Marker OH = new IconMarker("O’Dowd Hall", 42.6743275177939, -83.2153657078743, 0, Color.YELLOW, icon, 28);
        cachedMarkers.add(OH);

        Marker HH = new IconMarker("Hannah Hall", 42.67166426206644, -83.21786150336266, 0, Color.YELLOW, icon, 4);
        cachedMarkers.add(HH);

        Marker DH = new IconMarker("Dodge Hall", 42.671616685283276, -83.21662902832031, 0, Color.YELLOW, icon, 8);
        cachedMarkers.add(DH);

        Marker PH = new IconMarker("Pawley Hall", 42.671316926060534, -83.2106014341116, 0, Color.YELLOW, icon, 16);
        cachedMarkers.add(PH);

        Marker MSC = new IconMarker("Mathematics and Science Center", 42.67104551453034, -83.21782898157835, 0, Color.YELLOW, icon, 32);
        cachedMarkers.add(MSC);

        Marker EH = new IconMarker("Elliott Hall", 42.67195021502185, -83.2138167321682, 0, Color.YELLOW, icon, 64);
        cachedMarkers.add(EH);

        Marker VH = new IconMarker("Varner Hall", 42.671500824570884, -83.21282230317593, 0, Color.YELLOW, icon, 46);
        cachedMarkers.add(VH);

        Marker VDH = new IconMarker("Vandenberg Hall", 42.67688195694066, -83.21583610028028, 0, Color.YELLOW, icon, 5);
        cachedMarkers.add(VDH);

        Marker BB = new IconMarker("Belgian Barn", 42.67080195803381, -83.2189129292965, 0, Color.YELLOW, icon, 7);
        cachedMarkers.add(BB);

        Marker KML = new IconMarker("Kettering Magnetics Lab", 42.660792390349215, -83.21437798440456, 0, Color.YELLOW, icon, 49);
        cachedMarkers.add(KML);
        Marker OBS = new IconMarker("Observatory", 42.660182410654365, -83.2153831422329, 0, Color.YELLOW, icon, 343);
        cachedMarkers.add(OBS);

        Marker GHC = new IconMarker("Graham Health Center", 42.6744714743117, -83.21646206080914, 0, Color.YELLOW, icon, 17);
        cachedMarkers.add(GHC);

        Marker PSSB = new IconMarker("Police and Support Services Building", 42.67131791211299, -83.21899976581335, 0, Color.YELLOW, icon, 289);
        cachedMarkers.add(PSSB);

        Marker OVH = new IconMarker("Oak View Hall", 42.67804932754033, -83.21572311222553, 0, Color.YELLOW, icon, 3125);
        cachedMarkers.add(OVH);

        Marker FH = new IconMarker("Fitzgerald House", 42.67562681188996, -83.21485407650471, 0, Color.YELLOW, icon, 25);
        cachedMarkers.add(FH);

        Marker HiH = new IconMarker("Hill House", 42.6765154236834, -83.214973770082, 0, Color.YELLOW, icon, 125);
        cachedMarkers.add(HiH);

        Marker VWH = new IconMarker("Van Wagoner House", 42.67658813892651, -83.21435015648603, 0, Color.YELLOW, icon, 625);
        cachedMarkers.add(VWH);


        Marker HmH = new IconMarker("Hamlin Hall", 42.67710897477371, -83.21379460394382, 0, Color.YELLOW, icon, 85);
        cachedMarkers.add(HmH);


        Marker GTMA = new IconMarker("George T. Matthews Apartments", 42.67762019321994, -83.21193482726812, 0, Color.YELLOW, icon, 15625);
        cachedMarkers.add(GTMA);


        Marker AVNA = new IconMarker("Ann V. Nicholson Apartments", 42.679227767888584, -83.21049615740776, 0, Color.YELLOW, icon, 78125);
        cachedMarkers.add(AVNA);


        Marker AH = new IconMarker("Anibal House", 42.67578235068298, -83.21421034634113, 0, Color.YELLOW, icon, 1953125);
        cachedMarkers.add(AH);


        Marker PrH = new IconMarker("Priyale House", 42.67590017537326, -83.21324106305838, 0, Color.YELLOW, icon, 390625);
        cachedMarkers.add(PrH);

        Marker KL = new IconMarker("Kresge Library", 42.672655477850896, -83.21593601256609, 0, Color.YELLOW, icon, 19);
        cachedMarkers.add(KL);

        Marker ACO = new IconMarker("Athletics Center O’Rena", 42.67408619267781, -83.21286890655756, 0, Color.YELLOW, icon, 11);
        cachedMarkers.add(ACO);

        Marker PHL = new IconMarker("Pioneer Field (Lower Fields)", 42.673026964001444, -83.20769492536783, 0, Color.YELLOW, icon, 121);
        cachedMarkers.add(PHL);

        Marker UF = new IconMarker("Upper Fields", 42.67754550727726, -83.21012634783983, 0, Color.YELLOW, icon, 1331);
        cachedMarkers.add(UF);

        Marker OBF = new IconMarker("Oakland Baseball Field", 42.673324249925194, -83.21135580539703, 0, Color.YELLOW, icon, 14641);
        cachedMarkers.add(OBF);

        Marker MBT = new IconMarker("Meadow Brook Theater", 42.67601849282903, -83.2179731503129, 0, Color.YELLOW, icon, 23);
        cachedMarkers.add(MBT);

        Marker OC = new IconMarker("Oakland Center", 42.674288087759383, -83.2165153697133, 0, Color.YELLOW, icon, 529);
        cachedMarkers.add(OC);

        Marker MBMF = new IconMarker("Meadow Brook Musical Festival", 42.67629308705625, -83.20305772125721, 0, Color.YELLOW, icon, 124627);
        cachedMarkers.add(MBMF);

        Marker ST = new IconMarker("Sunset Terrace", 42.67598768101327, -83.198696449399, 0, Color.YELLOW, icon, 9);
        cachedMarkers.add(ST);

        Marker SGP = new IconMarker("Shotwell-Gustafson Pavilion", 42.6736952390709456, -83.19764737039804, 0, Color.YELLOW, icon, 2401);
        cachedMarkers.add(SGP);

        Marker CH = new IconMarker("Carriage House", 42.67271661175625, -83.20039495825768, 0, Color.YELLOW, icon, 81);
        cachedMarkers.add(CH);

        Marker GCC3PS = new IconMarker("Golf Course Clubhouse and Pro Shop", 42.67258546959509, -83.19449376314878, 0, Color.YELLOW, icon, 279841);
        cachedMarkers.add(GCC3PS);

        Marker MBG = new IconMarker("Meadow Brook Greenhouse", 42.673366155786844, -83.1945001333952, 0, Color.YELLOW, icon, 16807);
        cachedMarkers.add(MBG);

        Marker GODGC = new IconMarker("Grizzly Oaks Disc Golf Course", 42.67783118668519, -83.21774147450924, 0, Color.YELLOW, icon, 161051);
        cachedMarkers.add(GODGC);

        Marker CHP = new IconMarker("Central Heating Plant", 42.67504138108844, -83.21283269673586, 0, Color.YELLOW, icon, 13);
        cachedMarkers.add(CHP);

        Marker BGM = new IconMarker("Buildings and Grounds Maintenance", 42.669001631779544, -83.20853747427464, 0, Color.YELLOW, icon, 169);
        cachedMarkers.add(BGM);

        Marker SF = new IconMarker("Storage Facility", 42.668244064147785, -83.21281660348177, 0, Color.YELLOW, icon, 2197);
        cachedMarkers.add(SF);

        Marker ES = new IconMarker("Electrical Substation", 42.66749684147107, -83.216572701931, 0, Color.YELLOW, icon, 28561);
        cachedMarkers.add(ES);

        Marker FM = new IconMarker("Facilities Management", 42.67035034062799, -83.21831613779068, 0, Color.YELLOW, icon, 371293);
        cachedMarkers.add(FM);

        Marker BL = new IconMarker("Bear Lake", 42.676373690337826, -83.21662701666355, 0, Color.YELLOW, icon, 3);
        cachedMarkers.add(BL);

        Marker ET = new IconMarker("Elliott Tower", 42.67356730352934, -83.21549411863089, 0, Color.YELLOW, icon, 27);
        cachedMarkers.add(ET);

        Marker MBHG = new IconMarker("Meadow Brook Hall & Gardens", 42.67210847462645, -83.20127740502357, 0, Color.YELLOW, icon, 243);
        cachedMarkers.add(MBHG);

        Marker DC = new IconMarker("Danny’s Cabin", 42.672633538652455, -83.19691378623247, 0, Color.YELLOW, icon, 729);
        cachedMarkers.add(DC);

        Marker JDH = new IconMarker("John Dodge House", 42.67297544415698, -83.1947760656476, 0, Color.YELLOW, icon, 2187);
        cachedMarkers.add(JDH);




        return cachedMarkers;
    }

    public JSONObject getInfo()
    {
        return null;
    }
}
