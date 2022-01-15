package com.eneyeitech;

import java.sql.ResultSet;
import java.util.Collection;

//Interface that all CompanyDAOs must support
public interface CompanyDAO {
    public int insertCompany(String name);
    public boolean deleteCompany(Company company);
    public Company findCompany(int id);
    public boolean updateCompany(Company company);
    public ResultSet selectCompaniesRS();
    public Collection selectCompaniesTO();
}
