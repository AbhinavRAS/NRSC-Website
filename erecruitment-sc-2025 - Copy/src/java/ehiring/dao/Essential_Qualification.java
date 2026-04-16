/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehiring.dao;

/**
 *
 * @author ssd
 */
public class Essential_Qualification
{
    int essential_qualification_id;
    int eligibility_id;
    String qualification_value;

    public int getEssential_qualification_id() {
        return essential_qualification_id;
    }

    public void setEssential_qualification_id(int essential_qualification_id) {
        this.essential_qualification_id = essential_qualification_id;
    }

    public int getEligibility_id() {
        return eligibility_id;
    }

    public void setEligibility_id(int eligibility_id) {
        this.eligibility_id = eligibility_id;
    }

    public String getQualification_value() {
        return qualification_value;
    }

    public void setQualification_value(String qualification_value) {
        this.qualification_value = qualification_value;
    }
    
}
