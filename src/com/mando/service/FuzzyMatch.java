package com.mando.service;

import java.util.HashMap;

public class FuzzyMatch {
	
	private static HashMap <String,Double> typo;
	
	public static boolean isSimiliar(String a,String b) {
		return (getFuzzySimilarity2(a,b) >= 0.68);
		
		}
	
	private static void setup() {
			typo = new HashMap<String,Double>();
			double low_typo = 0.5;
			double med_typo = 0.6;
			double hi_typo = 0.7;
			
			typo.put("AQ",hi_typo);
			typo.put("AW",hi_typo);
			typo.put("ZA",hi_typo);
			typo.put("ZS",hi_typo);
			typo.put("QW",med_typo);
			typo.put("AS",med_typo);
			typo.put("ZX",med_typo);
			
			String atas   = "QWERTYUIOP";
			String tengah = "ASDFGHJKL";
			String bawah  = "ZXCVBNM";
			
			for (int i=1;i<tengah.length();i++)
				for (int j=i-1;j<i+1;j++){
					typo.put(""+tengah.charAt(i)+atas.charAt(j),hi_typo);
					typo.put(""+atas.charAt(i)+tengah.charAt(j),low_typo);
				}
			for (int i=1;i<bawah.length();i++)
				for (int j=i-1;j<i+1;j++){
					typo.put(""+bawah.charAt(i)+tengah.charAt(j),hi_typo);
					typo.put(""+tengah.charAt(i)+bawah.charAt(j),low_typo);
				}

			

			for (int i=1;i<atas.length()-1;i++) {
					typo.put(""+atas.charAt(i)+atas.charAt(i-1),med_typo);
					typo.put(""+atas.charAt(i)+atas.charAt(i+1),med_typo);
			}
			
			for (int i=1;i<tengah.length()-1;i++) {
					typo.put(""+tengah.charAt(i)+tengah.charAt(i-1),med_typo);
					typo.put(""+tengah.charAt(i)+tengah.charAt(i+1),med_typo);
			}
			
			for (int i=1;i<bawah.length()-1;i++) {
					typo.put(""+bawah.charAt(i)+bawah.charAt(i-1),med_typo);
					typo.put(""+bawah.charAt(i)+bawah.charAt(i+1),med_typo);
			}
			
			
			//fonem typo
			typo.put("FP",med_typo);
			typo.put("PF",med_typo);
			typo.put("OU",low_typo);
			typo.put("UO",low_typo);
			typo.put("IY",low_typo);
			typo.put("YI",low_typo);
		
		}
	
	public static String trigram(String a,int idx) {
			return ""+a.charAt(idx-1)+a.charAt(idx)+a.charAt(idx+1);
		}
	
	
	public static double getFuzzyScore(String a,String b) {
			if (typo == null)
				setup();
			String c = a+b;
			c = c.toUpperCase();
			if (typo.get(c)==null)	return 0.0;
			return typo.get(c);
		}
		
	public static double getFuzzySimilarity2(String a,String b) {
			
			double score = 0.0;
			a = "_"+a+" ";
			b = "_"+b+" ";
			
			int n = a.length();
			int m = b.length();
			
			
			//trigram search:
			for (int i=1;i<m-1;i++) {
					String t = trigram(b,i);
					
					//is found?
					boolean match = false;
					boolean common = false;
					double fuzzyMax = 0.0;
					String tmp="";
					for (int j=Math.max(1,i-2);j<Math.min(n-1,i+4);j++) {
							String x = trigram(a,j);
							if (x.equals(t)) match = true;
							if (b.charAt(i) == a.charAt(j)) {
									if (b.charAt(i-1) == a.charAt(j-1) ||
										b.charAt(i+1) == a.charAt(j-1) ||
										b.charAt(i-1) == a.charAt(j+1) ||
										b.charAt(i+1) == a.charAt(j+1) )
											common = true;
								}
						
							if (b.charAt(i-1) == a.charAt(j-1) && b.charAt(i+1) == a.charAt(j+1))
								{
									fuzzyMax = 1 + Math.max(fuzzyMax,getFuzzyScore(""+b.charAt(i),""+a.charAt(j)));
									//debug purpose
									//System.out.println(""+b.charAt(i)+a.charAt(j)+" -> "+getFuzzyScore(""+b.charAt(i),""+a.charAt(j)));
								}
							//flipped
							//System.out.println("comp "+b.charAt(i-1)+b.charAt(i)+b.charAt(i+1)+" <-> "+a.charAt(j-1)+a.charAt(j)+a.charAt(j+1));
							if (b.charAt(i-1) == a.charAt(j-1) && b.charAt(i) == a.charAt(j+1) && b.charAt(i+1) == a.charAt(j)){
								//	System.out.println("FLIPPED " + fuzzyMax+" "+common+" "+match);
									fuzzyMax = 1.9;
								}
							//appended
							if (a.length() > j + 2 && b.charAt(i-1) == a.charAt(j-1) && b.charAt(i) == a.charAt(j+1) &&
								b.charAt(i+1) == a.charAt(j+2))
							{
									fuzzyMax = 1 + Math.max(fuzzyMax,0.3 + getFuzzyScore(""+b.charAt(i),""+a.charAt(j)));
									//System.out.println("Appended " + fuzzyMax+" "+common+" "+match);
									fuzzyMax = 1 + Math.max(fuzzyMax,0.3 + getFuzzyScore(""+b.charAt(i-1),""+a.charAt(j)));
									//System.out.println("Appended " + fuzzyMax+" "+common+" "+match);
									
								}
							
						}
					if (match) score+=2.0;
					else if (common && fuzzyMax <= 1.5) {score += 1.5;
					}
					else {
						score += fuzzyMax;
					//	System.out.println(tmp+" : "+fuzzyMax);
					}
			
				}
			n-=2;
			m-=2;
			return score/(2.0*(m+Math.abs(n-m)));
		}
			
	public static double getSimilarity(String a,String b) {
			
			double score = 0.0;
			a = "_"+a+" ";
			b = "_"+b+" ";
			
			int n = a.length();
			int m = b.length();
			
			
			//trigram search:
			for (int i=1;i<m-1;i++) {
					String t = trigram(b,i);
					
					//is found?
					boolean match = false;
					boolean common = false;
					double fuzzyMax = 0.0;
					for (int j=Math.max(1,i-2);j<Math.min(n-1,i+4);j++) {
							String x = trigram(a,j);
							if (x.equals(t)) match = true;
							if (b.charAt(i) == a.charAt(j)) {
									if (b.charAt(i-1) == a.charAt(j-1) ||
										b.charAt(i+1) == a.charAt(j-1) ||
										b.charAt(i-1) == a.charAt(j+1) ||
										b.charAt(i+1) == a.charAt(j+1) )
											common = true;
								}
						
							if (b.charAt(i-1) == a.charAt(j-1) && b.charAt(i+1) == a.charAt(j+1))
								fuzzyMax = 1 + Math.max(fuzzyMax,0*getFuzzyScore(x,t));
						}
					if (match) score+=2.0;
					else if (common) score += 1.5;
					else score += fuzzyMax;
				
			
				}
			n-=2;
			m-=2;
			return score/(2.0*(m+Math.abs(n-m)));
		}
		
		
	public static double getFuzzySimilarity(String a,String b) {
			
			double score = 0.0;
			a = "_"+a+" ";
			b = "_"+b+" ";
			
			int n = a.length();
			int m = b.length();
			
			
			//trigram search:
			for (int i=1;i<m-1;i++) {
					String t = trigram(b,i);
					
					//is found?
					boolean match = false;
					boolean common = false;
					double fuzzyMax = 0.0;
					for (int j=Math.max(1,i-2);j<Math.min(n-1,i+4);j++) {
							String x = trigram(a,j);
							if (x.equals(t)) match = true;
							if (b.charAt(i) == a.charAt(j)) {
									if (b.charAt(i-1) == a.charAt(j-1) ||
										b.charAt(i+1) == a.charAt(j-1) ||
										b.charAt(i-1) == a.charAt(j+1) ||
										b.charAt(i+1) == a.charAt(j+1) )
											common = true;
								}
						
							if (b.charAt(i-1) == a.charAt(j-1) && b.charAt(i+1) == a.charAt(j+1))
								fuzzyMax = 1 + Math.max(fuzzyMax,getFuzzyScore(""+b.charAt(i),""+a.charAt(j)));
									
						}
					if (match) score+=2.0;
					else if (common) score += 1.5;
					else score += fuzzyMax;
				
			
				}
			n-=2;
			m-=2;
			return score/(2.0*(m+Math.abs(n-m)));
		}
	}
