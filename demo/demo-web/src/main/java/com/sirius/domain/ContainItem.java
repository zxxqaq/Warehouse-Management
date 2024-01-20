package com.sirius.domain;

public interface ContainItem {

    String getItemName();

    String getStandard();

    String getSpecification();

    String getMaterial();

    String getLevel();

    String getSurface();

    Double getUnitWeight();

    void setItemName(String itemName);

    public void setStandard(String standard);

    void setSpecification(String specification);

    void setMaterial(String material);

    void setLevel(String level);

    void setSurface(String surface);

    void setUnitWeight(Double unitWeight);

    String getUnit();

    void setUnit(String unit);


}
