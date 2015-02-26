package org.openmrs.module.printer.db;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.emrapi.db.HibernateSingleClassDAO;
import org.openmrs.module.printer.PrinterModel;
import org.openmrs.module.printer.PrinterType;

import java.util.List;

public class HibernatePrinterModelDAO extends HibernateSingleClassDAO<PrinterModel> implements PrinterModelDAO {

    public HibernatePrinterModelDAO() {
        super(PrinterModel.class);
    }

    @Override
    public List<PrinterModel> getPrinterModelsByType(PrinterType type) {
        Criteria criteria = createPrinterModelCriteria();
        addTypeRestriction(criteria, type);

        return (List<PrinterModel>) criteria.list();
    }

    @Override
    public boolean isNameAllocatedToAnotherPrinterModel(PrinterModel printerModel) {
        Criteria criteria = createPrinterModelCriteria();
        addNameRestriction(criteria, printerModel.getName());
        addUuidExclusionRestriction(criteria, printerModel.getUuid());
        Number count = (Number) criteria.setProjection(Projections.rowCount()).uniqueResult();

        return count.intValue() == 0 ? false : true;
    }

    private Criteria createPrinterModelCriteria() {
        return sessionFactory.getCurrentSession().createCriteria(PrinterModel.class);
    }

    private void addNameRestriction(Criteria criteria, String name) {
        criteria.add(Restrictions.eq("name", name));
    }

    private void addUuidExclusionRestriction(Criteria criteria, String uuid) {
        criteria.add(Restrictions.not(Restrictions.eq("uuid", uuid)));
    }

    public void addTypeRestriction(Criteria criteria, PrinterType type) {
        criteria.add(Restrictions.eq("type", type));
    }
}
