package entity;

public class StatisticReport {

	float median;
	float average;
	int[] histogram;

	public StatisticReport(float median, float average, int[] histogram) {
		this.median = median;
		this.average = average;
		this.histogram = histogram;
	}
	
}
