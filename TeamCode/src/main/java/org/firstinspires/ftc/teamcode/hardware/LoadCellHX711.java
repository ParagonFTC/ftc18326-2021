package org.firstinspires.ftc.teamcode.hardware;

import android.util.Log;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.util.TypeConversion;

@I2cDeviceType
@DeviceProperties(name = "LoadCell+HX711 Weight Sensor", xmlTag = "LCHX711")
public class LoadCellHX711 extends I2cDeviceSynchDevice<I2cDeviceSynch> {

    public final static I2cAddr ADDRESS_I2C_DEFAULT = new I2cAddr(0x64);

    public enum Register
    {
        FIRST(0x65),
        REG_DATA_GET_RAM_DATA(0x66),
        LAST(REG_DATA_GET_RAM_DATA.bVal),
        REG_DATA_INIT_SENSOR(0x70);

        public int bVal;

        Register(int bVal)
        {
            this.bVal = bVal;
        }
    }

    @Override
    public Manufacturer getManufacturer()
    {
        return Manufacturer.Other;
    }

/*
    protected void setOptimalReadWindow()
    {
        // Sensor registers are read repeatedly and stored in a register. This method specifies the
        // registers and repeat read mode
        I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(
                Register.FIRST.bVal,
                Register.LAST.bVal - Register.FIRST.bVal + 1,
                I2cDeviceSynch.ReadMode.REPEAT);
        this.deviceClient.setReadWindow(readWindow);
    }
 */

    @Override
    protected synchronized boolean doInitialize()
    {
        byte[] initSequence = new byte[] {(byte)0x65};
        deviceClient.write(Register.REG_DATA_INIT_SENSOR.bVal, initSequence);

        Log.i("LoadCell", "doInitialize() returning.");

        return true;
    }

    @Override
    public String getDeviceName()
    {
        return "LoadCell+HX711 Weight Sensor";
    }

    public LoadCellHX711(I2cDeviceSynch deviceClient)
    {
        super(deviceClient, true);

//        this.setOptimalReadWindow();
        this.deviceClient.setI2cAddress(ADDRESS_I2C_DEFAULT);

        super.registerArmingStateCallback(false);
        this.deviceClient.engage();
    }

    private int byte2int(byte aByte)
    {
        return ((int)aByte) & 0xff;
    }

    public int readWeight()
    {
        int weight = -1;
        byte[] ramData = deviceClient.read(Register.REG_DATA_GET_RAM_DATA.bVal, 8);
        Log.i("LoadCell", String.format("Ram data: 0x%x 0x%x 0x%x 0x%x 0x%x 0x%x 0x%x 0x%x", ramData[0], ramData[1], ramData[2], ramData[3], ramData[4], ramData[5], ramData[6], ramData[7]));

        if (ramData[2] == 0x12) {
            weight = (byte2int(ramData[3]) << 16) + (byte2int(ramData[4]) << 8) + byte2int(ramData[5]);
            Log.i("LoadCell", String.format("weight value: %d, in hex: 0x%x.", weight, weight));
        } else {
            Log.e("LoadCell", "Unexpected RAM data.");
        }

        return weight;
    }
}
