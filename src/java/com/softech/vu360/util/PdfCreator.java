package com.softech.vu360.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.model.LearnerAssignmentActivity;
import com.softech.vu360.lms.model.LearnerFinalCourseActivity;
import com.softech.vu360.lms.model.LearnerLectureCourseActivity;
import com.softech.vu360.lms.model.LearnerSelfStudyCourseActivity;
import com.softech.vu360.lms.web.controller.model.instructor.GradeBookForm;
import com.softech.vu360.lms.web.controller.model.instructor.ManageActivity;
import com.softech.vu360.lms.web.controller.model.instructor.ManageGrade;

/**
 * @author Dyutiman
 * created on 18 May 2010
 * Class used to create the printable pdf document of gradebook.
 *
 */
public class PdfCreator extends AbstractPdfView {

	//String documentPath = VU360Properties.getVU360Property("document.gradebook.pdfLocation");
	// merged from 4.7.2 branch
	String documentPath = VU360Properties.getVU360Property("document.gradebook.pdfLocationAbsolute");

	@SuppressWarnings("unchecked")
	public void buildPdfDocument( Map map, Document arg1, com.lowagie.text.pdf.PdfWriter arg2, HttpServletRequest arg3, HttpServletResponse arg4) 
	throws Exception {

		List<String> headers = new ArrayList<String>();
		headers.add("FIRST NAME");
		headers.add("LAST NAME");

		Document document = new Document();

		try {

			// Get an instance of PdfWriter and create a Table.pdf file as an output.
			com.lowagie.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(new File(documentPath+"Table.pdf")));
			document.open();
			document.addSubject(map.get("synClassName").toString());
			document.addTitle(map.get("synClassName").toString());

			PdfPTable headTitle = new PdfPTable(1);
			headTitle.setHorizontalAlignment(headTitle.ALIGN_CENTER);
			PdfPCell pCell = new PdfPCell();
			pCell.setGrayFill(1.0f);
			pCell.setPhrase(new Phrase(map.get("synClassName").toString().toUpperCase(), new Font(Font.HELVETICA, 10, Font.BOLD)));
			headTitle.addCell(pCell);
			headTitle.completeRow();
			document.add(headTitle);

			List<CourseActivity> courseActivities = (List<CourseActivity>)map.get("courseActivities");
			List<ManageActivity> activities = (List<ManageActivity>)map.get("activities");
			for( CourseActivity activity : courseActivities ) {
				headers.add(activity.getActivityName());
			}

			String[][] data = new String[activities.size()][headers.size()];

			for( int i = 0 ; i < activities.size() ; i ++  ) {
				ManageActivity mact = activities.get(i);
				data[i][0] = mact.getLearner().getVu360User().getFirstName();
				data[i][1] = mact.getLearner().getVu360User().getLastName();
				String actData = "";
				for( int j = 0 ; j < courseActivities.size() ; j ++ ) {

					if( mact.getLearnerCourseActivities().get(j) instanceof LearnerSelfStudyCourseActivity ) {
						LearnerSelfStudyCourseActivity act = (LearnerSelfStudyCourseActivity)mact.getLearnerCourseActivities().get(j);
						actData = act.getOverrideScore()+"";
					} else if( mact.getLearnerCourseActivities().get(j) instanceof LearnerFinalCourseActivity ) {
						LearnerFinalCourseActivity act = (LearnerFinalCourseActivity)mact.getLearnerCourseActivities().get(j);
						actData = act.getFinalRawScore()+"";
					} else if( mact.getLearnerCourseActivities().get(j) instanceof LearnerAssignmentActivity ) {
						actData = " ";
					} else if( mact.getLearnerCourseActivities().get(j) instanceof LearnerLectureCourseActivity ) {
						actData = " ";
					}
					data[i][j+2] = actData;
				}
			}

			// Create an instance of PdfPTable. After that we transform the header and
			// data array into a PdfPCell object. When each table row is complete we
			// have to call the table.completeRow() method.
			//
			// For better presentation we also set the cell font name, size and weight.
			// And we also define the background fill for the cell.
			//
			PdfPTable table = new PdfPTable(headers.size());
			for( int i = 0 ; i < headers.size() ; i++ ) {
				String header = headers.get(i);
				PdfPCell cell = new PdfPCell();
				cell.setGrayFill(0.9f);
				cell.setPhrase(new Phrase(header.toUpperCase(), new Font(Font.HELVETICA, 10, Font.BOLD)));
				table.addCell(cell);
			}
			table.completeRow();

			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					String datum = data[i][j];
					PdfPCell cell = new PdfPCell();
					cell.setPhrase(new Phrase(datum.toUpperCase(), new Font(Font.HELVETICA, 10, Font.NORMAL)));
					table.addCell(cell);
				}
				table.completeRow();
			}
			document.addTitle("Table Demo");
			document.add(table);

		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			document.close();
		}
	}

	public void buildGradeBookPdfDocument( GradeBookForm form ) 
	throws Exception {

		List<String> headers = new ArrayList<String>();
		headers.add("FIRST NAME");
		headers.add("LAST NAME");

		Document document = new Document();

		try {

			// Get an instance of PdfWriter and create a Table.pdf file as an output.
			com.lowagie.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(new File(documentPath+"GradeBook.pdf")));
			document.open();

			PdfPTable headTitle = new PdfPTable(1);
			headTitle.setHorizontalAlignment(headTitle.ALIGN_CENTER);
			PdfPCell pCell = new PdfPCell();
			pCell.setGrayFill(1.0f);
			pCell.setPhrase(new Phrase(form.getSynClassName().toUpperCase()+" -- "+form.getActivityName(), new Font(Font.HELVETICA, 10, Font.BOLD)));
			headTitle.addCell(pCell);
			headTitle.completeRow();
			document.add(headTitle);

			String[][] data = null;
			int s;

			if (form.getActType().equalsIgnoreCase(GradeBookForm.ASSIGNMENT_COURSE)) {
				s = 0;
				headers.add("ATTEMPTED");
				headers.add("COMPLETED");
				headers.add("SCORE");
				headers.add("RAW SCORE");
				data = new String[form.getManageGradeList().size()][headers.size()];
				for (ManageGrade mngGrade : form.getManageGradeList()) {
					data[s][0] = mngGrade.getUser().getFirstName();
					data[s][1] = mngGrade.getUser().getLastName();
					if (mngGrade.isAttempted())
						data[s][2] = "True";
					else
						data[s][2] = "False";

					if (mngGrade.isCompleted())
						data[s][3] = "True";
					else
						data[s][3] = "False";
					
					data[s][4] = mngGrade.getPercentScore();
					data[s][5] = mngGrade.getRawScore();
					s++;
				}
			} else if (form.getActType().equalsIgnoreCase(GradeBookForm.FINAL_SCORE_COURSE)) {
				s = 0;
				headers.add("COMPLETED");
				headers.add("COMPLETE DATE");
				headers.add("SCORE");
				headers.add("RAW SCORE");
				data = new String[form.getManageGradeList().size()][headers.size()];
				for (ManageGrade mngGrade : form.getManageGradeList()) {
					data[s][0] = mngGrade.getUser().getFirstName();
					data[s][1] = mngGrade.getUser().getLastName();
					if (mngGrade.isCourseComplete())
						data[s][2] = "True";
					else
						data[s][2] = "False";

					data[s][3] = mngGrade.getCourseCompleteDate();
					data[s][4] = mngGrade.getFinalPercentScore();
					data[s][5] = mngGrade.getFinalRawScore();
					s++;
				}
			} else if (form.getActType().equalsIgnoreCase(GradeBookForm.SELF_STUDY_COURSE)) {
				s = 0;
				headers.add("OVERRIDE");
				data = new String[form.getManageGradeList().size()][headers.size()];
				for (ManageGrade mngGrade : form.getManageGradeList()) {
					data[s][0] = mngGrade.getUser().getFirstName();
					data[s][1] = mngGrade.getUser().getLastName();
					data[s][2] = mngGrade.getOverride();
					s++;
				}
			} else if (form.getActType().equalsIgnoreCase(GradeBookForm.LECTURE_COURSE)) {
				s = 0;
				headers.add("ATTENDED");
				data = new String[form.getManageGradeList().size()][headers.size()];
				for (ManageGrade mngGrade : form.getManageGradeList()) {
					data[s][0] = mngGrade.getUser().getFirstName();
					data[s][1] = mngGrade.getUser().getLastName();
					if (mngGrade.isAttended())
						data[s][2] = "True";
					else
						data[s][2] = "False";
					s++;
				}
			} 

			// Create an instance of PdfPTable. After that we transform the header and
			// data array into a PdfPCell object. When each table row is complete we
			// have to call the table.completeRow() method.
			//
			// For better presentation we also set the cell font name, size and weight.
			// And we also define the background fill for the cell.
			//
			PdfPTable table = new PdfPTable(headers.size());
			for( int i = 0 ; i < headers.size() ; i++ ) {
				String header = headers.get(i);
				PdfPCell cell = new PdfPCell();
				cell.setGrayFill(0.9f);
				cell.setPhrase(new Phrase(header.toUpperCase(), new Font(Font.HELVETICA, 10, Font.BOLD)));
				table.addCell(cell);
			}
			table.completeRow();

			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					String datum = data[i][j];
					PdfPCell cell = new PdfPCell();
					cell.setPhrase(new Phrase(datum.toUpperCase(), new Font(Font.HELVETICA, 10, Font.NORMAL)));
					table.addCell(cell);
				}
				table.completeRow();
			}
			document.add(table);

		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			document.close();
		}
	}
}