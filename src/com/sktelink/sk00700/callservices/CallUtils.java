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
	
	public static final int BLOCK_DATA = 200;
	
	public static int TYPE_UPDATE_ALL = 0x0;
	public static int TYPE_UPDATE_BY_DAY = 0x1;
	
	public static Context context;

	
	/*
	 * TODO: constructor
	 */
	public CallUtils(Context _context) {
		context = _context;
	}
	

	////////////////////////////////////////////////////////////////////////////////

	
	/*
	 * TODO: update number in call log
	 */
	public void updateACallLog(ContentResolver resolver,
							  String oldNumber,
							  String newNumber) {
		Cursor cursor = null;
		try {
			ContentValues values = new ContentValues();
			values.put(CallLog.Calls.NUMBER, newNumber.toString());
			cursor = resolver.query(CallLog.Calls.CONTENT_URI, null,
							CallLog.Calls.NUMBER + "=? ", new String[] {oldNumber}, "");
			boolean bol = cursor.moveToFirst();
			if (bol) {
				do {
					int idOfRow = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));
					resolver.update(Uri.withAppendedPath(CallLog.Calls.CONTENT_URI,
														 String.valueOf(idOfRow)), values,
														 oldNumber, null);
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
								String targetPattern,
								List<ItemCallLog> listCallLog) {
		
		List<ItemCallLog> listData = listCallLog;

		// loop item call log
		for (ItemCallLog item : listData) {
			// update item number in call log
			updateACallLog(context.getContentResolver(),
						 item.getCallNumber(),
						 item.getNewNumber());
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
	
	
	/*
	 * TODO: get Count of Contact
	 */
	public int getCountContact () {
		Cursor countCursor = context.getContentResolver()
							.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
		return countCursor.getCount();
	}
	
	
	/*
	 * TODO: get list contact by block or all contact
	 */
	public List<ItemContact> getListContact (int type, int offset, long lastTime,
												List<String> listPatterns, String targetPattern) {
		
		List<ItemContact> listContact = new ArrayList<ItemContact>();
		Cursor managedCursor = null;
		String strOrder = CallLog.Calls.DATE + " DESC";
		Uri CONTENT_URI = CallLog.Calls.CONTENT_URI.buildUpon()
			                .appendQueryParameter("limit",String.valueOf(BLOCK_DATA))
			                .appendQueryParameter("offset",String.valueOf(offset)) 
			                .build();
		
		if (type == TYPE_UPDATE_ALL) {
			managedCursor = context.getContentResolver()
							.query(CONTENT_URI, null, null, null, strOrder);
		} else if (type == TYPE_UPDATE_BY_DAY) {
			managedCursor = context.getContentResolver()
							.query(CONTENT_URI, new String[]{String.valueOf(lastTime)},"DATE>= ?",null, strOrder);
		}
		
		listContact = listContact (managedCursor, listPatterns, targetPattern);
		
		return listContact;
	}
	
	
	/*
	 * TODO: list contact
	 */
	public List<ItemContact> listContact (Cursor managedCursor,
											List<String> listPatterns,
											String targetPattern) {
		
		List<ItemContact> listContact = new ArrayList<ItemContact>();
		
		
		return listContact;
	}
	
	
	/*
	 * TODO: get Count of Call Log
	 */
	public int getCountCallLog () {
		Cursor countCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
	                			null, null, null, null);
        return countCursor.getCount();
	}
	
	
	/*
	 * TODO: get call log by block or all call log
	 */
	public List<ItemCallLog> getListCallLog (int type, int offset, long lastTime,
												List<String> listPatterns, String targetPattern) {

		List<ItemCallLog> listCall = null;
		Cursor managedCursor = null;
		String strOrder = CallLog.Calls.DATE + " DESC ";
		Uri CONTENT_URI = CallLog.Calls.CONTENT_URI.buildUpon()
			                .appendQueryParameter("limit",String.valueOf(BLOCK_DATA))
			                .appendQueryParameter("offset",String.valueOf(offset)) 
			                .build();
		
		try {
			if (type == TYPE_UPDATE_ALL) {
				managedCursor = context.getContentResolver()
								.query(CONTENT_URI, null, null, null, strOrder);
			} else if (type == TYPE_UPDATE_BY_DAY) {
				managedCursor = context.getContentResolver()
								.query(CallLog.Calls.CONTENT_URI, new String[]{String.valueOf(lastTime)},CallLog.Calls.DATE + ">= ?",null, strOrder);
			}
			/* Query the CallLog Content Provider */
			managedCursor = context.getContentResolver().query(CONTENT_URI,
					null, null, null, strOrder);
			
			listCall = listCallLog(managedCursor, listPatterns, targetPattern);
			
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		} finally {
			// close cursor
			managedCursor.close();
		}
		return listCall;
	}
	
	/*
	 * TODO: return list call log
	 */
	private List<ItemCallLog> listCallLog (Cursor managedCursor,
											List<String> listPatterns,
											String targetPattern) {
		List<ItemCallLog> listCall = new ArrayList<ItemCallLog>();
		ItemCallLog item;
		
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
			// loop pattern
			for (int i = 0; i < listPatterns.size(); i++) {
				String patternWant = listPatterns.get(i);
				if (patternWant.length() > phNum.length()
					|| phNum.contains("*")
					|| phNum.contains("#")) {
					continue;
				}
				String pattern = phNum.substring(0, patternWant.length());

				// check string pattern contain pattern of number
				if (pattern.equals(patternWant)) {
					String newNumber = targetPattern
										+ phNum.substring(patternWant.length(), phNum.length());
					// setup data for item
					item.setCallNumber(phNum)
						.setCallDate(strcallDate)
						.setCallDuration(callDuration)
						.setCallType(callType);
					item.setNewNumber(newNumber);

					// add to list
					listCall.add(item);
				}// end-if
			}// end-for
		}// end-while
		return listCall;
	}
	
	
	
	
}
