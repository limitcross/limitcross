import 'package:flutter/material.dart';

import '../../models/service_item.dart';
import '../service_detail_screen.dart';
import '../../services/booking_service.dart';
import '../../widgets/booking_sheet.dart';

class ServicesTab extends StatefulWidget {
  final VoidCallback onNavigateToBookings;

  const ServicesTab({
    super.key,
    required this.onNavigateToBookings,
  });

  @override
  State<ServicesTab> createState() => _ServicesTabState();
}

class _ServicesTabState extends State<ServicesTab> {
  ServiceCategory _selectedCategory = ServiceCategory.all;
  final TextEditingController _searchController = TextEditingController();
  String _searchQuery = '';

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  List<ServiceItem> get _filteredServices {
    return ServiceItem.allServices.where((item) {
      final matchesCategory =
          _selectedCategory == ServiceCategory.all ||
          item.category == _selectedCategory;
      final matchesSearch =
          _searchQuery.isEmpty ||
          item.title.toLowerCase().contains(_searchQuery.toLowerCase()) ||
          item.description.toLowerCase().contains(_searchQuery.toLowerCase());
      return matchesCategory && matchesSearch;
    }).toList();
  }

  void _showCityPicker() {
    showModalBottomSheet<void>(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (ctx) {
        return SafeArea(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const Padding(
                padding: EdgeInsets.fromLTRB(20, 16, 20, 8),
                child: Text(
                  'Select Your City (51+ Cities Operating)',
                  style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                    color: Color(0xFF0F172A),
                  ),
                ),
              ),
              const Divider(),
              Expanded(
                child: ListView.builder(
                  itemCount: BookingService.availableCities.length,
                  itemBuilder: (context, idx) {
                    final city = BookingService.availableCities[idx];
                    final isSelected = city == BookingService().currentCity;
                    return ListTile(
                      title: Text(
                        city,
                        style: TextStyle(
                          fontWeight: isSelected
                              ? FontWeight.bold
                              : FontWeight.normal,
                        ),
                      ),
                      leading: Icon(
                        Icons.location_city_rounded,
                        color: isSelected
                            ? const Color(0xFF4F46E5)
                            : Colors.grey,
                      ),
                      trailing: isSelected
                          ? const Icon(
                              Icons.check_circle,
                              color: Color(0xFF4F46E5),
                            )
                          : null,
                      onTap: () {
                        BookingService().setCity(city);
                        Navigator.of(ctx).pop();
                        setState(() {});
                      },
                    );
                  },
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final popularServices = ServiceItem.allServices
        .where((s) => s.isPopular)
        .toList();

    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        titleSpacing: 16,
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                  Flexible(
                    child: const Text(
                      'Limitcross Facility',
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 16,
                        color: Color(0xFF0F172A),
                      ),
                    ),
                  ),
                  const SizedBox(width: 5),
                  Container(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 5,
                      vertical: 1,
                    ),
                    decoration: BoxDecoration(
                      color: const Color(0xFF4F46E5),
                      borderRadius: BorderRadius.circular(5),
                    ),
                    child: const Text(
                      'PRO',
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 9,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
              ],
            ),
            InkWell(
              onTap: _showCityPicker,
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  const Icon(
                    Icons.location_on,
                    size: 14,
                    color: Color(0xFF4F46E5),
                  ),
                  const SizedBox(width: 3),
                  Text(
                    BookingService().currentCity,
                    style: const TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w600,
                      color: Color(0xFF475569),
                    ),
                  ),
                  const Icon(
                    Icons.keyboard_arrow_down,
                    size: 16,
                    color: Color(0xFF475569),
                  ),
                ],
              ),
            ),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(
              Icons.event_note_outlined,
              color: Color(0xFF0F172A),
            ),
            tooltip: 'My Bookings',
            onPressed: widget.onNavigateToBookings,
          ),
          const SizedBox(width: 8),
        ],
      ),
      body: CustomScrollView(
        slivers: [
          // Search & Metrics Banner
          SliverToBoxAdapter(
            child: Container(
              color: Colors.white,
              padding: const EdgeInsets.fromLTRB(16, 8, 16, 16),
              child: Column(
                children: [
                  // Search Bar
                  TextField(
                    controller: _searchController,
                    onChanged: (val) => setState(() => _searchQuery = val),
                    decoration: InputDecoration(
                      hintText: 'Search "AC repair", "Deep clean", "Salon"...',
                      prefixIcon: const Icon(
                        Icons.search_rounded,
                        color: Color(0xFF64748B),
                      ),
                      suffixIcon: _searchQuery.isNotEmpty
                          ? IconButton(
                              icon: const Icon(Icons.clear, size: 18),
                              onPressed: () {
                                _searchController.clear();
                                setState(() => _searchQuery = '');
                              },
                            )
                          : null,
                      filled: true,
                      fillColor: const Color(0xFFF1F5F9),
                      contentPadding: const EdgeInsets.symmetric(
                        horizontal: 16,
                        vertical: 12,
                      ),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(14),
                        borderSide: BorderSide.none,
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),

          // Categories Horizontal Selector
          SliverToBoxAdapter(
            child: Container(
              color: Colors.white,
              padding: const EdgeInsets.only(bottom: 12),
              child: SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                padding: const EdgeInsets.symmetric(horizontal: 16),
                child: Row(
                  children: ServiceCategory.values.map((cat) {
                    final isSelected = _selectedCategory == cat;
                    return Padding(
                      padding: const EdgeInsets.only(right: 8),
                      child: FilterChip(
                        avatar: Text(
                          cat.iconEmoji,
                          style: const TextStyle(fontSize: 14),
                        ),
                        label: Text(cat.label),
                        selected: isSelected,
                        onSelected: (val) {
                          setState(() => _selectedCategory = cat);
                        },
                        selectedColor: const Color(0xFF0F172A),
                        labelStyle: TextStyle(
                          color: isSelected
                              ? Colors.white
                              : const Color(0xFF334155),
                          fontWeight: isSelected
                              ? FontWeight.bold
                              : FontWeight.w500,
                          fontSize: 12,
                        ),
                        backgroundColor: const Color(0xFFF1F5F9),
                        showCheckmark: false,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(12),
                          side: BorderSide(
                            color: isSelected
                                ? const Color(0xFF0F172A)
                                : const Color(0xFFE2E8F0),
                          ),
                        ),
                      ),
                    );
                  }).toList(),
                ),
              ),
            ),
          ),

          // Popular Carousel (Show when no search active)
          if (_searchQuery.isEmpty &&
              _selectedCategory == ServiceCategory.all) ...[
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Expanded(
                      child: Text(
                        '🔥 Trending & Most Booked',
                        style: TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF0F172A),
                        ),
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                    const SizedBox(width: 8),
                    Text(
                      '${popularServices.length} services',
                      style: TextStyle(
                        fontSize: 12,
                        color: Colors.grey.shade600,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            SliverToBoxAdapter(
              child: SizedBox(
                height: 156,
                child: ListView.builder(
                  scrollDirection: Axis.horizontal,
                  padding: const EdgeInsets.symmetric(horizontal: 16),
                  itemCount: popularServices.length,
                  itemBuilder: (context, index) {
                    final s = popularServices[index];
                    return InkWell(
                      borderRadius: BorderRadius.circular(16),
                      onTap: () {
                        Navigator.of(context).push(
                          MaterialPageRoute<void>(
                            builder: (_) => ServiceDetailScreen(service: s),
                          ),
                        );
                      },
                      child: Container(
                        width: 200,
                        margin: const EdgeInsets.only(right: 12),
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(16),
                          border: Border.all(color: const Color(0xFFE2E8F0)),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withValues(alpha: 0.03),
                              blurRadius: 8,
                              offset: const Offset(0, 2),
                            ),
                          ],
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                          Row(
                            children: [
                              Container(
                                width: 36,
                                height: 36,
                                decoration: BoxDecoration(
                                  color: s.accentColor.withValues(alpha: 0.12),
                                  borderRadius: BorderRadius.circular(10),
                                ),
                                alignment: Alignment.center,
                                child: Text(
                                  s.emoji,
                                  style: const TextStyle(fontSize: 20),
                                ),
                              ),
                              const Spacer(),
                              Container(
                                padding: const EdgeInsets.symmetric(
                                  horizontal: 6,
                                  vertical: 2,
                                ),
                                decoration: BoxDecoration(
                                  color: Colors.amber.shade50,
                                  borderRadius: BorderRadius.circular(6),
                                ),
                                child: Row(
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    const Icon(
                                      Icons.star_rounded,
                                      size: 14,
                                      color: Colors.amber,
                                    ),
                                    const SizedBox(width: 2),
                                    Text(
                                      '${s.rating}',
                                      style: const TextStyle(
                                        fontSize: 11,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 8),
                          Text(
                            s.title,
                            maxLines: 1,
                            overflow: TextOverflow.ellipsis,
                            style: const TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 13,
                              color: Color(0xFF0F172A),
                            ),
                          ),
                          Text(
                            s.priceRange,
                            style: const TextStyle(
                              fontWeight: FontWeight.w600,
                              fontSize: 12,
                              color: Color(0xFF10B981),
                            ),
                          ),
                          const Spacer(),
                          SizedBox(
                            height: 30,
                            width: double.infinity,
                            child: FilledButton(
                              onPressed: () async {
                                final booked = await BookingSheet.show(
                                  context,
                                  s,
                                );
                                if (booked == true && context.mounted) {
                                  ScaffoldMessenger.of(context).showSnackBar(
                                    SnackBar(
                                      content: Text(
                                        '🎉 ${s.title} scheduled successfully!',
                                      ),
                                      action: SnackBarAction(
                                        label: 'View',
                                        onPressed: widget.onNavigateToBookings,
                                      ),
                                    ),
                                  );
                                }
                              },
                              style: FilledButton.styleFrom(
                                backgroundColor: const Color(0xFF0F172A),
                                padding: EdgeInsets.zero,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(8),
                                ),
                              ),
                              child: const Text(
                                'Book',
                                style: TextStyle(
                                  fontSize: 12,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ),
                          ],
                        ),
                      ),
                    );
                  },
                ),
              ),
            ),
          ],

          // All / Filtered Services Section Header
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.fromLTRB(16, 20, 16, 10),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Expanded(
                    child: Text(
                      _selectedCategory == ServiceCategory.all
                          ? 'All Available Services'
                          : _selectedCategory.label,
                      style: const TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF0F172A),
                      ),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                  const SizedBox(width: 8),
                  Text(
                    '${_filteredServices.length} found',
                    style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
                  ),
                ],
              ),
            ),
          ),

          // Services Grid / List
          _filteredServices.isEmpty
              ? SliverToBoxAdapter(
                  child: Container(
                    padding: const EdgeInsets.all(40),
                    alignment: Alignment.center,
                    child: Column(
                      children: [
                        const Icon(
                          Icons.search_off_rounded,
                          size: 48,
                          color: Colors.grey,
                        ),
                        const SizedBox(height: 12),
                        Text(
                          'No services found for "$_searchQuery"',
                          style: const TextStyle(
                            fontWeight: FontWeight.w600,
                            color: Color(0xFF64748B),
                          ),
                        ),
                      ],
                    ),
                  ),
                )
              : SliverPadding(
                  padding: const EdgeInsets.fromLTRB(16, 0, 16, 24),
                  sliver: SliverList(
                    delegate: SliverChildBuilderDelegate((context, index) {
                      final s = _filteredServices[index];
                      return InkWell(
                        borderRadius: BorderRadius.circular(16),
                        onTap: () {
                          Navigator.of(context).push(
                            MaterialPageRoute<void>(
                              builder: (_) => ServiceDetailScreen(service: s),
                            ),
                          );
                        },
                        child: Container(
                          margin: const EdgeInsets.only(bottom: 12),
                          padding: const EdgeInsets.all(14),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(16),
                            border: Border.all(color: const Color(0xFFE2E8F0)),
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black.withValues(alpha: 0.02),
                                blurRadius: 6,
                                offset: const Offset(0, 2),
                              ),
                            ],
                          ),
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                            Container(
                              width: 50,
                              height: 50,
                              decoration: BoxDecoration(
                                color: s.accentColor.withValues(alpha: 0.12),
                                borderRadius: BorderRadius.circular(14),
                              ),
                              alignment: Alignment.center,
                              child: Text(
                                s.emoji,
                                style: const TextStyle(fontSize: 24),
                              ),
                            ),
                            const SizedBox(width: 14),
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Row(
                                    children: [
                                      Expanded(
                                        child: Text(
                                          s.title,
                                          style: const TextStyle(
                                            fontWeight: FontWeight.bold,
                                            fontSize: 15,
                                            color: Color(0xFF0F172A),
                                          ),
                                        ),
                                      ),
                                      Container(
                                        padding: const EdgeInsets.symmetric(
                                          horizontal: 6,
                                          vertical: 2,
                                        ),
                                        decoration: BoxDecoration(
                                          color: Colors.amber.shade50,
                                          borderRadius: BorderRadius.circular(
                                            6,
                                          ),
                                        ),
                                        child: Row(
                                          mainAxisSize: MainAxisSize.min,
                                          children: [
                                            const Icon(
                                              Icons.star_rounded,
                                              size: 14,
                                              color: Colors.amber,
                                            ),
                                            const SizedBox(width: 2),
                                            Text(
                                              '${s.rating}',
                                              style: const TextStyle(
                                                fontSize: 11,
                                                fontWeight: FontWeight.bold,
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ],
                                  ),
                                  const SizedBox(height: 4),
                                  Text(
                                    s.description,
                                    style: TextStyle(
                                      fontSize: 12,
                                      color: Colors.grey.shade600,
                                    ),
                                  ),
                                  const SizedBox(height: 8),
                                  Row(
                                    mainAxisAlignment:
                                        MainAxisAlignment.spaceBetween,
                                    children: [
                                      Flexible(
                                        child: Text(
                                          s.priceRange,
                                          overflow: TextOverflow.ellipsis,
                                          style: const TextStyle(
                                            fontWeight: FontWeight.bold,
                                            fontSize: 14,
                                            color: Color(0xFF059669),
                                          ),
                                        ),
                                      ),
                                      FilledButton(
                                        onPressed: () async {
                                          final booked =
                                              await BookingSheet.show(
                                                context,
                                                s,
                                              );
                                          if (booked == true &&
                                              context.mounted) {
                                            ScaffoldMessenger.of(
                                              context,
                                            ).showSnackBar(
                                              SnackBar(
                                                content: Text(
                                                  '🎉 ${s.title} scheduled!',
                                                ),
                                                action: SnackBarAction(
                                                  label: 'View',
                                                  onPressed: widget
                                                      .onNavigateToBookings,
                                                ),
                                              ),
                                            );
                                          }
                                        },
                                        style: FilledButton.styleFrom(
                                          backgroundColor: const Color(
                                            0xFF0F172A,
                                          ),
                                          minimumSize: const Size(72, 34),
                                          padding: const EdgeInsets.symmetric(
                                            horizontal: 10,
                                          ),
                                          shape: RoundedRectangleBorder(
                                            borderRadius: BorderRadius.circular(
                                              8,
                                            ),
                                          ),
                                        ),
                                        child: const Text(
                                          'Book',
                                          style: TextStyle(
                                            fontSize: 12,
                                            fontWeight: FontWeight.bold,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),
                            ],
                          ),
                        ),
                      );
                    }, childCount: _filteredServices.length),
                  ),
                ),
        ],
      ),
    );
  }
}
