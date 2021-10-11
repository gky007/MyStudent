package com.guffly.course10;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
	List list = new ArrayList<Object>();
	Student student1 = new Student(1,1,1);
	Student student2 = new Student(2,2,3);
	Student student3 = new Student(3,2,4);
	Student student4 = new Student(4,1,6);
	Student student5 = new Student(5,2,1);
	list.add(student1);
	list.add(student2);
	list.add(student3);
	list.add(student4);
	list.add(student5);
	for (int i = 0; i < list.size(); i++) {
	    System.out.println(list.get(i));
	}
    }
}
class Student{
	Integer id;
	Integer lineSort;
	Integer colSort;
	
	public Student(Integer id, Integer lineSort, Integer colSort) {
	    this.id = id;
	    this.lineSort = lineSort;
	    this.colSort = colSort;
	}

	public Integer getId() {
	    return id;
	}


	public void setId(Integer id) {
	    this.id = id;
	}


	public Integer getLineSort() {
	    return lineSort;
	}
	public void setLineSort(Integer lineSort) {
	    this.lineSort = lineSort;
	}
	public Integer getColSort() {
	    return colSort;
	}
	public void setColSort(Integer colSort) {
	    this.colSort = colSort;
	}

//	@Override
//	public int hashCode() {
//	    final int prime = 31;
//	    int result = 1;
//	    result = prime * result + ((colSort == null) ? 0 : colSort.hashCode());
//	    result = prime * result + ((id == null) ? 0 : id.hashCode());
//	    result = prime * result + ((lineSort == null) ? 0 : lineSort.hashCode());
//	    return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//	    if (this == obj)
//		return true;
//	    if (obj == null)
//		return false;
//	    if (getClass() != obj.getClass())
//		return false;
//	    Student other = (Student) obj;
//	    if (colSort == null) {
//		if (other.colSort != null)
//		    return false;
//	    } else if (!colSort.equals(other.colSort))
//		return false;
//	    if (id == null) {
//		if (other.id != null)
//		    return false;
//	    } else if (!id.equals(other.id))
//		return false;
//	    if (lineSort == null) {
//		if (other.lineSort != null)
//		    return false;
//	    } else if (!lineSort.equals(other.lineSort))
//		return false;
//	    return true;
//	}

	@Override
	public String toString() {
	    return "Student [id=" + id + ", lineSort=" + lineSort + ", colSort=" + colSort + "]";
	}


}
