/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

/**
 *
 * @author Zion Tseu
 */
import da.StudentDA;
import domain.Student;
public class MaintainStudent {
    private StudentDA studDA;
    
    public MaintainStudent(){
        studDA = new StudentDA();
    }
    
    public Student selectRecord(String id){
        return studDA.getRecord(id);
    }
    
    public void addRecord(Student p){
        studDA.addRecord(p);
    }
    
    public void updateRecord(Student p){
        studDA.updateRecord(p);
    }
    
    public void deleteRecord(String id){
        studDA.deleteRecord(id);
    }
}
