package com.po;

public class MachineInfo {
    private int numOfMa;
    private FEntry [] machines;
    public MachineInfo(){
    }
    public MachineInfo(int[]data) throws Exception{
        if(data.length!=(data[0]*2+1)){
            throw new Exception("the number of the entry is wrong!");
        }
        this.numOfMa=data[0];
        machines=new FEntry[data[0]];
        for(int i=0;i<machines.length;++i){
            machines[i]=new FEntry(data[2*i+1],data[2*i+2]);
        }
    }
    public int getNumOfMa() {
        return numOfMa;
    }

    public void setNumOfMa(int numOfMa) {
        this.numOfMa = numOfMa;
    }

    public FEntry[] getMachines() {
        return machines;
    }

    public void setMachines(FEntry[] machines) {
        this.machines = machines;
    }

    public class FEntry{
        private int ma;
        private int time;
        public FEntry(){}

        public FEntry(int ma,int time){
            this.ma=ma;
            this.time=time;
        }
        public int getMa() {
            return ma;
        }

        public void setMa(int ma) {
            this.ma = ma;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
