package org.example.bloodbank;

import org.example.database.Donor;

import java.util.ArrayList;
import java.util.List;

public class GetDonorFromDatabase
{
    List<DonorData> list;
    static  GetDonorFromDatabase object;
    private GetDonorFromDatabase()
    {
        list=new ArrayList<>();
    }
    static GetDonorFromDatabase getInstance()
    {
        if(object==null)
            object=new GetDonorFromDatabase();
        return object;
    }
    void addDonor(DonorData data)
    {
        boolean check=true;
        for(int i=0;i<list.size();i++){
            if(list.get(i).name==data.name&&list.get(i).address==data.address&&
                    list.get(i).contact==data.contact&&list.get(i).age==data.age
                    &&list.get(i).bloodGroup==data.bloodGroup){
                check=false;
            }
        }
        if(check) {
            DonorData d = new DonorData(data.name, data.address, data.contact, data.age, data.bloodGroup);
            list.add(d);
        }
    }
}
