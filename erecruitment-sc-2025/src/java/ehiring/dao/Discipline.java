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
public class Discipline
{
    int discipline_id;
    int essential_qualification_id;
    int eligibility_id;
    String discipline_value;

    public int getDiscipline_id() {
        return discipline_id;
    }

    public void setDiscipline_id(int discipline_id) {
        this.discipline_id = discipline_id;
    }

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

    public String getDiscipline_value() {
        return discipline_value;
    }

    public void setDiscipline_value(String discipline_value) {
        this.discipline_value = discipline_value;
    }
}
