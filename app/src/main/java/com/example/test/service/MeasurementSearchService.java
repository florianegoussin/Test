package com.example.test.service;

//import com.example.test.model.LocationCoordinates;

public class MeasurementSearchService {
    /*
    private static final long REFRESH_DELAY = 650;
    public static MeasurementSearchService INSTANCE = new MeasurementSearchService();
    private final MeasurementSearchRESTService mMesureSearchRESTService;
    private ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture mLastScheduleTask;

    private MeasurementSearchService(){
        // Create GSON Converter that will be used to convert from JSON to Java
        Gson gsonConverter = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation().create();

        // Create Retrofit client
        Retrofit retrofit = new Retrofit.Builder()
                // Using OkHttp as HTTP Client
                .client(new OkHttpClient())
                // Having the following as server URL
                .baseUrl("https://api.openaq.org/v1/")
                // Using GSON to convert from Json to Java
                .addConverterFactory(GsonConverterFactory.create(gsonConverter))
                .build();

        // Use retrofit to generate our REST service code
        mMesureSearchRESTService = retrofit.create(MeasurementSearchRESTService.class);

        public  void searchMesures(final String search) {

            if (mLastScheduleTask != null && !mLastScheduleTask.isDone()) {
                mLastScheduleTask.cancel(true);
            }


            // Schedule a network call in REFRESH_DELAY ms
            mLastScheduleTask = mScheduler.schedule(new Runnable() {
                public void run() {
                    searchMesuresFromDB(search);

                    // Call to the REST service
                    //Modification pour uniquement les locations en France
                    mMesureSearchRESTService.searchForMesures("FR",search).enqueue(new Callback<MesureSearchResult>() {
                        @Override
                        public void onResponse(Call<MesureSearchResult> call, retrofit2.Response<MesureSearchResult> response) {
                            // Post an event so that listening activities can update their UI
                            if (response.body() != null && response.body().results != null) {
                                EventBusManager.BUS.post(new MesureResultEvent(response.body().results));
                                ActiveAndroid.beginTransaction();
                                for (Measurement mesure : response.body().results) {
                                    Measurement m = new Measurement();
                                    m.parameter = mesure.parameter;
                                    m.value = mesure.value;
                                    m.save();
                                }
                                ActiveAndroid.setTransactionSuccessful();
                                ActiveAndroid.endTransaction();


                                searchMesuresFromDB(search);

                            } else {
                                // Null result
                                // We may want to display a warning to user (e.g. Toast)
                            }
                        }


                        @Override
                        public void onFailure(Call<MesureSearchResult> call, Throwable t) {
                            // Request has failed or is not at expected format
                            // We may want to display a warning to user (e.g. Toast)
                            Log.e("[PlaceSearcher] [REST]", "Response error : " + t.getMessage());
                            //searchLocationsFromDB(search);
                        }

                    });
                }

            },REFRESH_DELAY, TimeUnit.MILLISECONDS);
        }

        public void searchMesuresFromDB (String search){
            List<Measurement> matchingMesureFromDB = new Select()
                    .from(Measurement.class)
                    .where("city LIKE '%" + search + "%'").orderBy("city ").execute();

            EventBusManager.BUS.post(new MesureResultEvent(matchingMesureFromDB));
        }

        // Service describing the REST APIs
        public interface MeasurementSearchRESTService {
            @GET("cities/")
            Call<MesureSearchResult> searchForMesures(@Query("country") String search);

        }

    }

*/

}


