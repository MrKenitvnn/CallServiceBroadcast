package com.sktelink.sk00700.callservices;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class CallUtils {
	
	public static int TYPE_FIRST_RUN = 0x0;
	public static int TYPE_BY_TIME	 = 0x1;

	public static Context context;

	// //////////////////////////////////////////////////////////////////////////////
	// TODO constructor

	public CallUtils(Context _context) {
		context = _context;
	}

	// //////////////////////////////////////////////////////////////////////////////
	// TODO function

	/*
	 * TODO: update number in call log
	 */
	public void updateNumber(ContentResolver resolver,
							  String strNumberOne[],
							  String newNumber) {
		Cursor cursor = null;
		try {
			String whereNumber = strNumberOne[0];

			ContentValues values = new ContentValues();
			values.put(CallLog.Calls.NUMBER, newNumber);
			cursor = resolver.query(CallLog.Calls.CONTENT_URI, null,
							CallLog.Calls.NUMBER + " = ? ", strNumberOne, "");
			boolean bol = cursor.moveToFirst();
			if (bol) {
				do {
					int idOfRowToDelete = cursor.getInt(cursor
							.getColumnIndex(CallLog.Calls._ID));
					resolver.update(Uri.withAppendedPath(
							CallLog.Calls.CONTENT_URI,
							String.valueOf(idOfRowToDelete)), values,
							whereNumber, null);
				} while (cursor.moveToNext());
			}
		} catch (Exception ex) {
			cursor.close();
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		} finally {
			cursor.close();
		}
	}// end-func updateNumber
	
	
	/*
	 * TODO: update CallLog
	 */
	public void updateCallLog (Context context,
								List<String> listPatterns,
								String targetPattern) {

		List<ItemCallLog> listData = null;
		String[] arrUpdate;
		
		listData = getAllCallLog(context);

		// loop item call log
		for (ItemCallLog item : listData) {
			// loop pattern
			for (int i = 0; i < listPatterns.size(); i++) {
				String patternWant = listPatterns.get(i);
				String pattern = item.getCallNumber().substring(0,
						patternWant.length());

				// check string pattern contain pattern of number
				if (pattern.equals(patternWant)) {
					String newNumber = targetPattern
										+ item.getCallNumber()
											  .substring(patternWant.length(), item.getCallNumber().length());
					
					// update item number in call log
					arrUpdate = new String[] { item.getCallNumber() };
					updateNumber(context.getContentResolver(), arrUpdate, newNumber);
					
				}// end-if
			}// end-for
		}// end-for
	}// end-func updateCallLog
	
	
	public void updateAllContact (ContentResolver resolver, List<String> listPattern, String targetPattern) {
		Cursor cur = null;
		try {
			boolean notSemantic = false;
			String newNumber = "";
	        cur = resolver.query(ContactsContract.Contacts.CONTENT_URI,
	                null, null, null, null);
	        if (cur.getCount() > 0) {
	            while (cur.moveToNext()) {
	                  String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
	                  if (Integer.parseInt(cur.getString(
	                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
	                     Cursor pCur = resolver.query(
	                               ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	                               null,
	                               ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
	                               new String[]{id}, null);
	                     while (pCur.moveToNext()) {
	                         String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                         
	                      // loop pattern
	             			for (int i = 0; i < listPattern.size(); i++) {
	             				
	             				String patternWant = listPattern.get(i);
	             				String pattern = phoneNo.substring(0, patternWant.length());
	             				notSemantic = false;
	             				
	             				if (phoneNo.contains(")")) {
	             					pattern = phoneNo.substring(0, patternWant.length() +2)
	         								.replace("(", "")
	         								.replace(")", "");
	             					notSemantic = true;
	             				}
	             				
                                                                                                                                                  	             				// check string pattern contain pattern of number
	             				if (pattern.equals(patternWant)) {
	             					if(notSemantic){
	             						newNumber= targetPattern
	      									   + phoneNo.substring(patternWant.length() +2, phoneNo.length());
	             					} else {
	             						newNumber = targetPattern
	      									   + phoneNo.substring(patternWant.length(), phoneNo.length());
	             					}
	             					// update item number in call log
	             					String[] arrUpdate = new String[] { phoneNo };
	             					updateAContact(resolver, arrUpdate, newNumber.replace("(", "").replace(")", ""));
	             				}// end-if
	             			}// end-for
	                     }// end-while
	                     pCur.close();
	                }// end-if
	            }// end-while
	        }// end-if
		} catch (Exception ex) {
			cur.close();
			Log.d(">>> trams <<<", Log.getStackTraceString(ex) );
		} finally {
			cur.close();
		}
	}// end-func updateAllContact
	
	
	public void updateAContact (ContentResolver resolver, String[] current, String target) {       
		try{
			String contactName = getContactName(context, current[0]);			

			if (contactName != null) {
				String where = ContactsContract.Data.DISPLAY_NAME+ " = ? AND " + 
	                    ContactsContract.Data.MIMETYPE + " = ? ";

	            String[] params = new String[] {contactName,
	                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};

	            ArrayList<ContentProviderOperation> ops =
	                   new ArrayList<ContentProviderOperation>();

	            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
	                    .withSelection(where, params)
	                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, target )
	                    .build());
	            
	            // finally call update
	            resolver.applyBatch(ContactsContract.AUTHORITY, ops);
			}// end-if
		} catch (Exception e) {
			Log.d(">>> trams <<<", Log.getStackTraceString(e) );
        }// end-try
    }// end-func update contact
	
	
	public static String getContactName (Context context, String phoneNumber) {
	    String contactName = null;
	    Cursor cursor = null;
		try {
		    ContentResolver cr = context.getContentResolver();
		    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		    cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
		    if (cursor == null) {
		        return null;
		    }
		    if(cursor.moveToFirst()) {
		        contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		    }
	
		    if(cursor != null && !cursor.isClosed()) {
		        cursor.close();
		    }
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		} finally {
			cursor.close();
		}
	    return contactName;
	}// end-func getContactName
	
	
	private List<ItemCallLog> getAllCallLog (Context context) {
		
		List<ItemCallLog> listCall = new ArrayList<ItemCallLog>();
		ItemCallLog item;
		Cursor managedCursor = null;
		
		try {
			String strOrder = CallLog.Calls.DATE + " DESC";
	
			/* Query the CallLog Content Provider */
			managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
					null, null, null, strOrder);
	
			int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
			int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
			int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
			int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
	
			while (managedCursor.moveToNext()) {
				item = new ItemCallLog();
	
				String phNum = managedCursor.getString(number);
				String callTypeCode = managedCursor.getString(type);
				String strcallDate = managedCursor.getString(date);
				String callDuration = managedCursor.getString(duration);
				String callType = managedCursor.getString(type);
				int callcode = Integer.parseInt(callTypeCode);
				switch (callcode) {
				case CallLog.Calls.OUTGOING_TYPE:
					callType = "Outgoing Call";
					break;
				case CallLog.Calls.INCOMING_TYPE:
					callType = "Incoming Call";
					break;
				case CallLog.Calls.MISSED_TYPE:
					callType = "Missed Call";
					break;
				}
	
				// setup data for item
				item.setCallNumber(phNum)
					.setCallDate(strcallDate)
					.setCallDuration(callDuration)
					.setCallType(callType);
	
				// add to list
				listCall.add(item);
			}// end-while
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		} finally {
			// close cursor
			managedCursor.close();
		}

		return listCall;
	}// end-func getAllCallLog
	
}
