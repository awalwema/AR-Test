package com.example.andrew.ar_test.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

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

        setMarkers();
    }

    protected void createIcon(Resources res) {
        if (res == null) throw new NullPointerException();

        icon = BitmapFactory.decodeResource(res, R.drawable.oubear);
    }

    private void setMarkers(){
        // Educational Buildings (Mainly)
        Marker SFH = new IconMarker("South Foundation Hall", 42.673648896361826, -83.21774147450924,
                0, Color.YELLOW, 2, icon);
        cachedMarkers.add(SFH);

        Marker EC = new IconMarker("Engineering Center", 42.67175571785303, -83.2150337854087, 0,
                Color.YELLOW, 476, icon);
        cachedMarkers.add(EC);

        Marker HHB = new IconMarker("Human Health Building", 42.67746835635772, -83.2193286716938,
                0, Color.YELLOW, 14, icon);
        cachedMarkers.add(HHB);

        Marker OH = new IconMarker("O’Dowd Hall", 42.6743275177939, -83.2153657078743, 0,
                Color.YELLOW, 28, icon);
        cachedMarkers.add(OH);

        Marker HH = new IconMarker("Hannah Hall", 42.67166426206644, -83.21786150336266, 0,
                Color.YELLOW, 4, icon);
        cachedMarkers.add(HH);

        Marker DH = new IconMarker("Dodge Hall", 42.671616685283276, -83.21662902832031, 0,
                Color.YELLOW, 8, icon);
        cachedMarkers.add(DH);

        Marker PH = new IconMarker("Pawley Hall", 42.671316926060534, -83.2106014341116, 0,
                Color.YELLOW, 16, icon);
        cachedMarkers.add(PH);

        Marker MSC = new IconMarker("Mathematics and Science Center", 42.67104551453034,
                -83.21782898157835, 0, Color.YELLOW, 32, icon);
        cachedMarkers.add(MSC);

        Marker EH = new IconMarker("Elliott Hall", 42.67195021502185, -83.2138167321682, 0,
                Color.YELLOW, 64, icon);
        cachedMarkers.add(EH);

        Marker VH = new IconMarker("Varner Hall", 42.671500824570884, -83.21282230317593, 0,
                Color.YELLOW, 46, icon);
        cachedMarkers.add(VH);

        // Labs (Mainly)
        Marker BB = new IconMarker("Belgian Barn", 42.67080195803381, -83.2189129292965, 0,
                Color.YELLOW, 7, icon);
        cachedMarkers.add(BB);

        Marker KML = new IconMarker("Kettering Magnetics Lab", 42.660792390349215,
                -83.21437798440456, 0, Color.YELLOW, 49, icon);
        cachedMarkers.add(KML);

        Marker OBS = new IconMarker("Observatory", 42.660182410654365, -83.2153831422329, 0,
                Color.YELLOW, 343, icon);
        cachedMarkers.add(OBS);

        Marker SGP = new IconMarker("Shotwell-Gustafson Pavilion", 42.6736952390709456,
                -83.19764737039804, 0, Color.YELLOW, 2401, icon);
        cachedMarkers.add(SGP);

        Marker MBG = new IconMarker("Meadow Brook Greenhouse", 42.673366155786844,
                -83.1945001333952, 0, Color.YELLOW, 16807, icon);
        cachedMarkers.add(MBG);

        // Administration Buildings (Mainly)
        Marker NFH = new IconMarker("North Foundation Hall", 42.67426835748442, -83.21780283004045,
                0, Color.YELLOW, 34, icon);
        cachedMarkers.add(NFH);

        Marker GHC = new IconMarker("Graham Health Center", 42.67675254900997, -83.21801975369453,
                0, Color.YELLOW, 17, icon);
        cachedMarkers.add(GHC);

        Marker PSSB = new IconMarker("Police and Support Services Building", 42.67131791211299,
                -83.21899976581335, 0, Color.YELLOW, 289, icon);
        cachedMarkers.add(PSSB);

        // Dorms/Apartments (Mainly)
        Marker VDH = new IconMarker("Vandenberg Hall", 42.67688195694066, -83.21583610028028, 0,
                Color.YELLOW, 5, icon);
        cachedMarkers.add(VDH);

        Marker OVH = new IconMarker("Oak View Hall", 42.67804932754033, -83.21572311222553, 0,
                Color.YELLOW, 3125, icon);
        cachedMarkers.add(OVH);

        Marker FH = new IconMarker("Fitzgerald House", 42.67562681188996, -83.21485407650471, 0,
                Color.YELLOW, 25, icon);
        cachedMarkers.add(FH);

        Marker HiH = new IconMarker("Hill House", 42.6765154236834, -83.214973770082, 0,
                Color.YELLOW, 125, icon);
        cachedMarkers.add(HiH);

        Marker VWH = new IconMarker("Van Wagoner House", 42.67658813892651, -83.21435015648603, 0,
                Color.YELLOW, 625, icon);
        cachedMarkers.add(VWH);

        Marker HmH = new IconMarker("Hamlin Hall", 42.67710897477371, -83.21379460394382, 0,
                Color.YELLOW, 85, icon);
        cachedMarkers.add(HmH);


        Marker GTMA = new IconMarker("George T. Matthews Apartments", 42.67762019321994,
                -83.21193482726812, 0, Color.YELLOW, 15625, icon);
        cachedMarkers.add(GTMA);


        Marker AVNA = new IconMarker("Ann V. Nicholson Apartments", 42.679227767888584,
                -83.21049615740776, 0, Color.YELLOW, 78125, icon);
        cachedMarkers.add(AVNA);


        Marker AH = new IconMarker("Anibal House", 42.67578235068298, -83.21421034634113, 0,
                Color.YELLOW, 1953125, icon);
        cachedMarkers.add(AH);


        Marker PrH = new IconMarker("Priyale House", 42.67590017537326, -83.21324106305838, 0,
                Color.YELLOW, 390625, icon);
        cachedMarkers.add(PrH);

        // Library
        Marker KL = new IconMarker("Kresge Library", 42.672655477850896, -83.21593601256609, 0,
                Color.YELLOW, 19, icon);
        cachedMarkers.add(KL);

        // Sports/Recreational Facilities
        Marker REC = new IconMarker("Rec Center", 42.673605265162614, -83.2130965590477, 0,
                Color.YELLOW, 17, icon);
        cachedMarkers.add(REC);

        Marker ACO = new IconMarker("Athletics Center O’Rena", 42.674084467162885,
                -83.21367457509041, 0, Color.YELLOW, 11, icon);
        cachedMarkers.add(ACO);

        Marker PHL = new IconMarker("Pioneer Field (Lower Fields)", 42.67472734140899,
                -83.21095749735832, 0, Color.YELLOW, 121, icon);
        cachedMarkers.add(PHL);

        Marker UF = new IconMarker("Recreation and Athletic Complex", 42.677864708945506, -83.21027956902981, 0,
                Color.YELLOW, 1331, icon);
        cachedMarkers.add(UF);

        Marker OBF = new IconMarker("Oakland Baseball Field", 42.673324249925194,
                -83.21135580539703, 0, Color.YELLOW, 14641, icon);
        cachedMarkers.add(OBF);

        Marker GODGC = new IconMarker("Grizzly Oaks Disc Golf Course", 42.67780752390234,
                -83.20705153048038, 0, Color.YELLOW, 161051, icon);
        cachedMarkers.add(GODGC);

        Marker GCC3PS = new IconMarker("Golf Course Clubhouse and Pro Shop", 42.67258546959509,
                -83.19449376314878, 0, Color.YELLOW, 1771561, icon);
        cachedMarkers.add(GCC3PS);

        // Entertainment
        Marker MBT = new IconMarker("Meadow Brook Theater", 42.67601849282903, -83.2179731503129,
                0, Color.YELLOW, 23, icon);
        cachedMarkers.add(MBT);

        Marker OC = new IconMarker("Oakland Center", 42.673981922189625, -83.21694150567055, 0,
                Color.YELLOW, 529, icon);
        cachedMarkers.add(OC);

        Marker MBMF = new IconMarker("Meadow Brook Music Festival", 42.67629308705625,
                -83.20305772125721, 0, Color.YELLOW, 124627, icon);
        cachedMarkers.add(MBMF);

        // Utilities Facilities
        Marker CHP = new IconMarker("Central Heating Plant", 42.67504138108844, -83.21283269673586,
                0, Color.YELLOW, 13, icon);
        cachedMarkers.add(CHP);

        Marker BGM = new IconMarker("Buildings and Grounds Maintenance", 42.669001631779544,
                -83.20853747427464, 0, Color.YELLOW, 169, icon);
        cachedMarkers.add(BGM);

        Marker SF = new IconMarker("Storage Facility", 42.668244064147785, -83.21281660348177, 0,
                Color.YELLOW, 2197, icon);
        cachedMarkers.add(SF);

        Marker ES = new IconMarker("Electrical Substation", 42.66749684147107, -83.216572701931, 0,
                Color.YELLOW, 28561, icon);
        cachedMarkers.add(ES);

        Marker FM = new IconMarker("Facilities Management", 42.67035034062799, -83.21831613779068,
                0, Color.YELLOW, 371293, icon);
        cachedMarkers.add(FM);

        // Sightseeing
        Marker ST = new IconMarker("Sunset Terrace", 42.67598768101327, -83.198696449399, 0,
                Color.YELLOW, 9, icon);
        cachedMarkers.add(ST);

        Marker CH = new IconMarker("Carriage House", 42.67271661175625, -83.20039495825768, 0,
                Color.YELLOW, 81, icon);
        cachedMarkers.add(CH);

        Marker BL = new IconMarker("Bear Lake", 42.67629998887037, -83.21636818349361, 0,
                Color.YELLOW, 3, icon);
        cachedMarkers.add(BL);

        Marker ET = new IconMarker("Elliott Tower", 42.673552020267834, -83.21567080914974, 0,
                Color.YELLOW, 27, icon);
        cachedMarkers.add(ET);

        Marker MBHG = new IconMarker("Meadow Brook Hall & Gardens", 42.67210847462645,
                -83.20127740502357, 0, Color.YELLOW, 243, icon);
        cachedMarkers.add(MBHG);

        Marker DC = new IconMarker("Danny’s Cabin", 42.672633538652455, -83.19691378623247, 0,
                Color.YELLOW, 729, icon);
        cachedMarkers.add(DC);

        Marker JDH = new IconMarker("John Dodge House", 42.67297544415698, -83.1947760656476, 0,
                Color.YELLOW, 2187, icon);
        cachedMarkers.add(JDH);
    }

    public List<Marker> getMarkers() {
        if(cachedMarkers==null)
            Log.e("cachedMakers == null: ", "Markers not loaded correctly.");

        return cachedMarkers;
    }

    /**
     * Filter already set list of markers to a list of specific type.
     *
     * @param type -- Identifier for filtering.
     * @return New list of filtered markers.
     */
    public List<Marker> filterType(int type){
        List<Marker> temp = new ArrayList<Marker>();

        for(int i=0; i<cachedMarkers.size(); i++)
        {
            if(cachedMarkers.get(i).getType()%type==0)
                temp.add(cachedMarkers.get(i));
        }

        return temp;
    }

    public List<Marker> filterByName(String name){

        List<Marker> temp2 = new ArrayList<Marker>();


        for(int i=0; i<cachedMarkers.size(); i++)
        {
            if(cachedMarkers.get(i).getName().equalsIgnoreCase(name))
                temp2.add(cachedMarkers.get(i));
        }

        return temp2;
    }

    public JSONObject getInfo()
    {
        return null;
    }
}
