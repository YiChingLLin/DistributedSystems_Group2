from multiprocessing import Process,freeze_support
import raw_data_creator

if __name__ == '__main__':
    freeze_support()
    process_list = [Process(target=raw_data_creator.run1), Process(target=raw_data_creator.run2), Process(target=raw_data_creator.run3)]
    for p in process_list:
        p.start()
    for p in process_list:
        p.join()
