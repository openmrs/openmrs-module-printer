package org.openmrs.module.printer;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.User;

import java.util.Date;

public class PrinterModel extends BaseOpenmrsObject {

    private Integer printerModelId;

    private String name;

    private PrinterType type;

    private String printHandler;

    private User creator;

    private Date dateCreated;

    @Override
    public Integer getId() {
        return printerModelId;
    }

    @Override
    public void setId(Integer id) {
        printerModelId = id;
    }

    public Integer getPrinterModelId() {
        return printerModelId;
    }

    public void setPrinterModelId(Integer printerModelId) {
        this.printerModelId = printerModelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrinterType getType() {
        return type;
    }

    public void setType(PrinterType type) {
        this.type = type;
    }

    public String getPrintHandler() {
        return printHandler;
    }

    public void setPrintHandler(String printHandler) {
        this.printHandler = printHandler;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
