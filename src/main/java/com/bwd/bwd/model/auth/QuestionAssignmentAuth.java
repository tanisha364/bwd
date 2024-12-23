package com.bwd.bwd.model.auth;

import com.bwd.bwd.util.DateTimeCreation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "questionnaire_assignment")
@Getter
@Setter
public class QuestionAssignmentAuth {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "que_assignment_Id")
	    private int que_assignment_Id;

	    @Column(name = "useraccountid")
		private int useraccountid;

	    @Column(name = "assigned_by")
	    private int assigned_by;

	    @Column(name = "sequence")
	    private int sequence;
	    
	    @Column(name = "test_id")
	    private int test_id;
	    
	    @Column(name = "date_assigned")
	   	public java.sql.Timestamp date_assigned;
	    
	    @Column(name = "archive")
	    private int archived;
	    
		
	    public QuestionAssignmentAuth()
		{
			
		}

		
		public QuestionAssignmentAuth createAss(int useraccountid, int assigned_by, int sequence, int test_id, int archived)
		{
			java.sql.Timestamp dt = DateTimeCreation.getSqlTimestamp();
			this.useraccountid = useraccountid;
			this.assigned_by = assigned_by;
			this.sequence = sequence;
			this.test_id = test_id;
			this.archived = archived;

			this.date_assigned = (java.sql.Timestamp)dt;
			return this;
		}
	}

