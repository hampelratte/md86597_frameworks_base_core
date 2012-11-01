// [BEGIN] skyviia modify: add for passing mounted info to UI!

package android.os.storage;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * @author markyang
 *
 */
public class MountInfo implements Parcelable {
	
    protected ArrayList<MountStorage> mEntries = new ArrayList<MountStorage>();
    
    public MountInfo() {

    }
	
    public MountInfo(ArrayList<MountStorage> entries) {
        addList(entries);
    }

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	public int describeContents() {
		return 0;
	}

    public void addList(ArrayList<MountStorage> entries) {
        clear();
        mEntries.addAll(entries);
    }

    public void clear() {
        mEntries.clear();
    }

    public void addOneEntry(MountStorage oneEntry) {
        mEntries.add(oneEntry);
    }

    public ArrayList<MountStorage> getAllEntries() {
        return mEntries;
    }

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel out, int flags) {
        int count = mEntries.size();
        out.writeInt(count);
        for (int i=0; i<count; i++) {
            MountStorage oneEntry = mEntries.get(i);
        	out.writeString(oneEntry.getType().toString());
            out.writeString(oneEntry.getPath());
            out.writeString(oneEntry.getSourceId());
            out.writeString(oneEntry.getURL());
            out.writeString(oneEntry.getFriendlyName());
	    out.writeString(oneEntry.getState());
            out.writeString(oneEntry.getSSize());
        }
	}

	public static TYPES checkType(String type) {
		if (type.equals(TYPES.MMC.toString())) {
			return TYPES.MMC;
		} else if (type.equals(TYPES.USB.toString())) {
			return TYPES.USB;
		} else if (type.equals(TYPES.SCSI.toString())) {
 			return TYPES.SCSI;
 		} else if (type.equals(TYPES.BLOCK.toString())) {
			return TYPES.BLOCK;
		} else if (type.equals(TYPES.SAMBA.toString())) {
            return TYPES.SAMBA;
        } else if (type.equals(TYPES.DLNA.toString())) {
            return TYPES.DLNA;
        } else if (type.equals(TYPES.DEVMAPPER.toString())) {
            return TYPES.DEVMAPPER;
        }else if (type.equals(TYPES.HDD.toString())) {
	    	return TYPES.HDD; //victor add hdd type
		}else
			return TYPES.UNKNOWN;
	}
	
    public static final Parcelable.Creator<MountInfo> CREATOR
            = new Parcelable.Creator<MountInfo>() {
        public MountInfo createFromParcel(Parcel in) {
            MountInfo mntInfo = new MountInfo();
            mntInfo.clear();
            int N = in.readInt();
            for (int i=0; i<N; i++) {
            	mntInfo.addOneEntry( new MountInfo.MountStorage(
    				checkType(in.readString()), // type
    				in.readString(), //path
                    in.readString(), //source Id
                    in.readString(), //URL
                    in.readString(), //friendly name
                    in.readString(),	 //media state
                    in.readString()) //partition sector size
        		);
            }
            return mntInfo;
        }

        public MountInfo[] newArray(int size) {
            return new MountInfo[size];
        }
    };

    public static class MountStorage {
    	TYPES mType; /* The mount type! */
        String mFriendlyName; /* For DLNA & Samba */
        String mSourceId; /* For DLNA & Samba to identify source/server */
        String mURL; /* For DLNA URL */
    	String mMntPoint; /* The mount point at local */
	String mState;  //charleskao,20101229
        String mSSize; /*The sector size for high bit rate m2ts playing*/
    	
    	public MountStorage(TYPES type, String path, String sourceId, String url, String friendlyName, String state, String SSize) {
    		mType = type; 
    		mMntPoint = path;
            mSourceId = sourceId;
            mFriendlyName = friendlyName;
            mURL = url;
	    mState = state;
            mSSize = SSize;
    	}
    	
    	public TYPES getType() {
    		return mType;
    	}
    	
    	public String getPath() {
    		return mMntPoint;
    	}

    	public String getFriendlyName() {
    		return mFriendlyName;
    	}

    	public String getSourceId() {
    		return mSourceId;
    	}

    	public String getURL() {
    		return mURL;
    	}

	public String getState(){
		return mState;
	}

        public String getSSize(){
            return mSSize;
        }		
    }
    
    public enum TYPES {
    	UNKNOWN("unknown"), MMC("mmc"), USB("usb"), SCSI("scsi"), DEVMAPPER("devmapper"), BLOCK("block"), DLNA("dlna"), SAMBA("samba"),HDD("hdd");
    	
    	private String mStrType;
    	
    	TYPES(String strType) {
    		mStrType = strType;
    	}
    	
    	public String toString() {
    		return this.mStrType;
    	}
    }
    
}

// [END]